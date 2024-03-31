package org.example.ratelimiterlearning.limiter;

import java.util.concurrent.TimeoutException;

public interface RateLimiter {
    public void execute(long timeoutMillis) throws TimeoutException;
}
