package org.example.ratelimiterlearning.limiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

public class LeakyBucketLimiter {
    private static final Logger logger = LoggerFactory.getLogger(LeakyBucketLimiter.class);
    private final AtomicLong requestIds = new AtomicLong(0);
    private final LeakyBucket bucket;

    public LeakyBucketLimiter(LeakyBucket leakyBucket) {
        this.bucket = leakyBucket;
        this.bucket.beginLeak();
    }

    public void tryRun() {
        var id = requestIds.getAndIncrement();
        bucket.add(id);
    }
}
