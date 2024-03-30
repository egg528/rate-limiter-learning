package org.example.ratelimiterlearning.limiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class LeakyBucket {
    private static final Logger logger = LoggerFactory.getLogger(LeakyBucket.class);
    private final BlockingQueue<Long> waitingQueue;
    private final int flowRate;
    private final TimeUnit timeUnit;

    public LeakyBucket(int capacity, int flowRate, TimeUnit timeUnit) {
        this.flowRate = flowRate;
        this.timeUnit = timeUnit;
        this.waitingQueue = new ArrayBlockingQueue<>(capacity);
    }

    public void add(long id) {
        try {
            waitingQueue.add(id);
        } catch (Exception e) {
            throw new IllegalStateException("Too Many Request");
        }
    }

    public void beginLeak() {
        var scheduleService = Executors.newScheduledThreadPool(1);
        scheduleService.scheduleAtFixedRate(() -> {
            if(!waitingQueue.isEmpty()) {
                waitingQueue.poll();
            }
        }, 0, flowRate, timeUnit);
    }
}
