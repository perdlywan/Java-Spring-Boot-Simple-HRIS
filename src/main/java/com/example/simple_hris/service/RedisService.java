package com.example.simple_hris.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    // ========================
    // GET
    // ========================
    public <T> T get(String key, Class<T> clazz) {
        // fetch from redis
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value != null) {
                return clazz.cast(value);
            }
        } catch (Exception e) {
            log.error("Redis GET failed, fallback to local cache",e);
        }

        return null;
    }

    // ========================
    // SET
    // ========================
    public void set(String key, Object value, Duration ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
        } catch (Exception e) {
            log.error("Redis SET failed, save to local cache");
        }
    }

    // ========================
    // DELETE
    // ========================
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis DELETE failed, remove from local cache");
        }
    }

}
