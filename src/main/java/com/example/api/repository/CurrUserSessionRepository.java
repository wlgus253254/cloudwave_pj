package com.example.api.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CurrUserSessionRepository {

    private final RedisTemplate<String, String> redisTemplate;  // 변경된 부분

    public CurrUserSessionRepository(RedisTemplate<String, String> redisTemplate) {  // 변경된 부분
        this.redisTemplate = redisTemplate;
    }

    public void saveSession(String sessionId, Long userId) {  // 변경된 부분
        redisTemplate.opsForValue().set(sessionId, String.valueOf(userId));  // 변경된 부분
    }

    public Long getSession(String sessionId) {  // 변경된 부분
        String userIdStr = redisTemplate.opsForValue().get(sessionId);  // 변경된 부분
        return userIdStr != null ? Long.parseLong(userIdStr) : null;  // 변경된 부분
    }

    public void deleteSession(String sessionId) {
        redisTemplate.delete(sessionId);
    }
}
