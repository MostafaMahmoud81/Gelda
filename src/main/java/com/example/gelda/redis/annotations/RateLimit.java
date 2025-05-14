package com.example.gelda.redis.annotations;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    long limit();
    long duration();
    TimeUnit timeUnit() default TimeUnit.SECONDS;
    String keyPrefix() default "";
}