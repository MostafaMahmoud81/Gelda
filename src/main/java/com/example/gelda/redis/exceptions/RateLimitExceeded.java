package com.example.gelda.redis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitExceeded extends RuntimeException {
    public RateLimitExceeded(String msg) {
        super(msg);
    }
}
