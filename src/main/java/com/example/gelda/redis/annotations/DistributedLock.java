package com.example.gelda.redis.annotations;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * Annotation for acquiring a distributed lock using Redis.
 * Can be applied on methods to ensure only one execution at a time
 * for the same resource identifier.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * Prefix to use for the Redis lock key.
     */
    String keyPrefix();

    /**
     * SpEL expression to dynamically evaluate the key identifier from method arguments.
     */
    String keyIdentifierExpression();

    /**
     * Lease time (duration the lock should be held before automatic expiration).
     */
    long leaseTime() default 30;

    /**
     * Time unit for the lease time.
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}