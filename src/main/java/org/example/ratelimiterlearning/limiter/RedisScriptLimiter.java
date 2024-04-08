package org.example.ratelimiterlearning.limiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.TimeoutException;


@Component
public class RedisScriptLimiter implements RateLimiter {

    private static final Logger logger = LoggerFactory.getLogger(LeakyBucket.class);
    private final RedisScript<Long> script;
    private final RedisTemplate<String, String> redisTemplate;

    public RedisScriptLimiter(RedisScript<Long> script, RedisTemplate<String, String> redisTemplate) {
        this.script = script;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void execute(long timeoutMillis) {

        try {
            var result = this.redisTemplate.execute(script, Collections.singletonList("limiterKey"), "1000", "10");
            System.out.println(result);
            if (result.equals(0L)) {
                throw new IllegalStateException("Too Many Request");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
