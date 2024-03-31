package org.example.ratelimiterlearning.limiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class LeakyBucket {
    private static final Logger logger = LoggerFactory.getLogger(LeakyBucket.class);
    private final BlockingQueue<Long> waitingQueue;
    private final Set<Long> waitingIds;
    private final int flowRate;
    private final TimeUnit timeUnit;

    public LeakyBucket(int capacity, int flowRate, TimeUnit timeUnit) {
        this.flowRate = flowRate;
        this.timeUnit = timeUnit;
        this.waitingQueue = new ArrayBlockingQueue<>(capacity);
        this.waitingIds = new HashSet<>();
    }

    public void add(long id) {
        try {
            waitingIds.add(id);
            waitingQueue.add(id);
        } catch (Exception e) {
            waitingIds.remove(id);
            throw new IllegalStateException("Too Many Request");
        }
    }

    public void beginLeak() {
        var scheduleService = Executors.newScheduledThreadPool(1);
        scheduleService.scheduleAtFixedRate(() -> {
            if(!waitingQueue.isEmpty()) {
                var id = waitingQueue.poll();
                waitingIds.remove(id);
            }
        }, 0, flowRate, timeUnit);
    }

    public boolean isWaiting(long id) {
        return waitingQueue.contains(id);
    }
}
