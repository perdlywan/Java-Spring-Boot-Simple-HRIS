package com.example.simple_hris.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void get_shouldReturnObject_whenKeyExists() {
        String key = "testKey";
        String value = "testValue";
        when(valueOperations.get(key)).thenReturn(value);

        String result = redisService.get(key, String.class);

        assertEquals(value, result);
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void get_shouldReturnNull_whenKeyDoesNotExist() {
        String key = "nonExistent";
        when(valueOperations.get(key)).thenReturn(null);

        String result = redisService.get(key, String.class);

        assertNull(result);
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void get_shouldReturnNull_whenExceptionOccurs() {
        String key = "errorKey";
        when(valueOperations.get(key)).thenThrow(new RuntimeException("Redis error"));

        String result = redisService.get(key, String.class);

        assertNull(result);
        verify(valueOperations, times(1)).get(key);
    }

    @Test
    void set_shouldCallRedisTemplate_whenRequestIsValid() {
        String key = "testKey";
        String value = "testValue";
        Duration ttl = Duration.ofMinutes(5);

        redisService.set(key, value, ttl);

        verify(valueOperations, times(1)).set(key, value, ttl);
    }

    @Test
    void delete_shouldCallRedisTemplate_whenKeyIsProvided() {
        String key = "testKey";

        redisService.delete(key);

        verify(redisTemplate, times(1)).delete(key);
    }
}
