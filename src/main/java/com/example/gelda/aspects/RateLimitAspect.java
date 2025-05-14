package com.example.gelda.aspects;

import com.example.gelda.redis.annotations.RateLimit;
import com.example.gelda.redis.exceptions.RateLimitExceeded;
import com.example.gelda.redis.service.Redis;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
@Order(1)
public class RateLimitAspect {

    private final Redis redisClient;
    private final HttpServletRequest request;

    @Autowired
    public RateLimitAspect(Redis redisClient, HttpServletRequest request) {
        this.redisClient = redisClient;
        this.request = request;
    }

    @Around("@annotation(rateLimit)")
    public Object enforceRateLimit(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        final String clientIp = getClientIp();
        final String redisKey = String.format("rate:%s:%s", rateLimit.keyPrefix(), clientIp);
        final long windowSeconds = rateLimit.timeUnit().toSeconds(rateLimit.duration());

        Long requestCount = redisClient.increment(redisKey, Duration.ofSeconds(windowSeconds));

        if (requestCount != null && requestCount > rateLimit.limit()) {
            throw new RateLimitExceeded("Too many requests. Please wait.");
        }

        return joinPoint.proceed();
    }

    private String getClientIp() {
        String ip = request.getRemoteAddr();
        return (ip != null) ? ip : "unknown";
    }
}