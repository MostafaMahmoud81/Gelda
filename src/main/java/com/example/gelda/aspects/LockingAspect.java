package com.example.gelda.aspects;

import com.example.gelda.redis.annotations.DistributedLock;
import com.example.gelda.redis.exceptions.LockAcquisition;
import com.example.gelda.redis.service.Redis;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.UUID;

@Aspect
@Component
public class LockingAspect {

    private final Redis redisClient;
    private final ExpressionParser parser = new SpelExpressionParser();

    @Autowired
    public LockingAspect(Redis redisClient) {
        this.redisClient = redisClient;
    }

    @Around("@annotation(distributedLock)")
    public Object handleLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // Prepare SpEL context with method arguments
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = methodSignature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        String keySuffix = parser
                .parseExpression(distributedLock.keyIdentifierExpression())
                .getValue(context, String.class);

        final String lockKey = String.format("lock:%s:%s", distributedLock.keyPrefix(), keySuffix);
        final String lockValue = UUID.randomUUID().toString();

        final Duration leaseDuration = Duration.of(distributedLock.leaseTime(), distributedLock.timeUnit().toChronoUnit());
        final boolean isLockAcquired = Boolean.TRUE.equals(redisClient.setIfAbsent(lockKey, lockValue, leaseDuration));

        if (!isLockAcquired) {
            throw new LockAcquisition("Resource is currently locked. Please try again later.");
        }

        try {
            return joinPoint.proceed();
        } finally {
            // Optional: You could verify the lock value before deletion to avoid deleting someone else's lock
            redisClient.delete(lockKey);
        }
    }
}
