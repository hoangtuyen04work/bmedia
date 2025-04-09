package io.github.hoangtuyen04work.social_backend.services.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenRedisService {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveToken(String id, String token) {
        deleteToken(id);
        String key = "access_token:" + id;
        redisTemplate.opsForValue().set(key, token, Duration.ofHours(24));
    }

    public String getToken(String id) {
        String key = "access_token:" + id;
        Object value = redisTemplate.opsForValue().get(key);
        String x = value.toString();
        return x.replaceAll("^\"|\"$", "");
    }

    public void deleteToken(String id){
        String key = "access_token:" + id;
        redisTemplate.delete(key);
    }
}
