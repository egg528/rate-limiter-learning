package org.example.ratelimiterlearning.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ratelimiterlearning.limiter.LeakyBucketLimiter;
import org.example.ratelimiterlearning.limiter.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.logging.Logger;

@Component
public class RateLimitFilter implements Filter {
    private static final Logger logger = Logger.getLogger(RateLimitFilter.class.getName());
    private static final long timeoutMillis = Duration.ofSeconds(5).toMillis();
    private final RateLimiter limiter;

    public RateLimitFilter(RateLimiter limiter) {
        this.limiter = limiter;
    }

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
