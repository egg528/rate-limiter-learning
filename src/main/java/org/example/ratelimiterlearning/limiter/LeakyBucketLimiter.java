package org.example.ratelimiterlearning.limiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class LeakyBucketLimiter implements RateLimiter {
    private static final Logger logger = LoggerFactory.getLogger(LeakyBucketLimiter.class);
    private final AtomicLong requestIds = new AtomicLong(0);
    private final LeakyBucket bucket;

    public LeakyBucketLimiter(LeakyBucket leakyBucket) {
        this.bucket = leakyBucket;
        this.bucket.beginLeak();
    }

    public void execute(long timeoutMillis) throws TimeoutException {
        var id = requestIds.getAndIncrement();
        bucket.add(id);

        checkExecuted(id, timeoutMillis);
    }

    private void checkExecuted(long id, long timeoutMillis) throws TimeoutException {
        long startTime = System.currentTimeMillis();

        while(System.currentTimeMillis() - startTime < timeoutMillis) {
            if (!bucket.isWaiting(id)) {
                return;
            }
        }

        throw new TimeoutException("Waiting timed out. Please retry.");
    }
}
