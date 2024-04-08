package org.example.ratelimiterlearning.config;

import org.example.ratelimiterlearning.filter.RateLimitFilter;
import org.example.ratelimiterlearning.limiter.RateLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RateLimitFilter> myFilterRegistration(RateLimitFilter limitFilter) {
        FilterRegistrationBean<RateLimitFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(limitFilter);
        registration.addUrlPatterns("/*"); // 필터가 적용될 URL 패턴 설정
        return registration;
    }
}
