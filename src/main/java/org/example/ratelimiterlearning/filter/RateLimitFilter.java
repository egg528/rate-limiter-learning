package org.example.ratelimiterlearning.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.example.ratelimiterlearning.controller.TestController;
import org.example.ratelimiterlearning.limiter.LeakyBucket;
import org.example.ratelimiterlearning.limiter.LeakyBucketLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
public class RateLimitFilter implements Filter {
    private static final Logger logger = Logger.getLogger(RateLimitFilter.class.getName());
    LeakyBucketLimiter limiter = new LeakyBucketLimiter(
            new LeakyBucket(1, 1000, TimeUnit.MILLISECONDS)
    );

    @Autowired
    public TestController controller;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {
            limiter.tryRun();
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Too Many Request");
        }
    }

    @Override
    public void destroy() {
        logger.info("================ DESTROY RATE LIMIT FILTER ================");
        Filter.super.destroy();
    }
}
