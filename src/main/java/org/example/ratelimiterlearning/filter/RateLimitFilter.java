package org.example.ratelimiterlearning.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ratelimiterlearning.limiter.LeakyBucket;
import org.example.ratelimiterlearning.limiter.LeakyBucketLimiter;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class RateLimitFilter implements Filter {
    private static final Logger logger = Logger.getLogger(RateLimitFilter.class.getName());
    private static final long timeoutMillis = Duration.ofSeconds(5).toMillis();
    private static final LeakyBucketLimiter limiter = new LeakyBucketLimiter(
            new LeakyBucket(1, 1000, TimeUnit.MILLISECONDS)
    );



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            limiter.execute(timeoutMillis);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Too Many Request");
        }
    }
}
