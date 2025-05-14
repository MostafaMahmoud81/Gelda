package com.example.gelda.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * A lightweight Redis client wrapper to handle string-based Redis operations.
 */
@Service
public class Redis {

    private final StringRedisTemplate redisTemplate;
    private final ValueOperations<String, String> valueOps;

    @Autowired
    public Redis(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
    }

    /**
     * Set a key-value pair in Redis with no expiration.
     */
    public void set(String key, String value) {
        valueOps.set(key, value);
    }

    /**
     * Set a key-value pair in Redis with a timeout.
     */
    public void set(String key, String value, Duration timeout) {
        valueOps.set(key, value, timeout);
    }

    /**
     * Get a value by key from Redis.
     */
    public String get(String key) {
        return valueOps.get(key);
    }

    /**
     * Delete a key from Redis.
     */
    public boolean delete(String key) {
        Boolean result = redisTemplate.delete(key);
        return Boolean.TRUE.equals(result);
    }

    /**
     * Set a key-value pair only if the key does not already exist, with expiration.
     */
    public boolean setIfAbsent(String key, String value, Duration timeout) {
        Boolean result = valueOps.setIfAbsent(key, value, timeout);
        return Boolean.TRUE.equals(result);
    }

    /**
     * Increment the value of a key by 1. If this is the first increment, set a TTL.
     */
    public long increment(String key, Duration ttl) {
        Long count = valueOps.increment(key);
        if (Long.valueOf(1).equals(count)) {
            redisTemplate.expire(key, ttl);
        }
        return count != null ? count : 0;
    }
}