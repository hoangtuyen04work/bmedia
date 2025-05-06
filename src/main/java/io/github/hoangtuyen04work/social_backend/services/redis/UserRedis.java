package io.github.hoangtuyen04work.social_backend.services.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hoangtuyen04work.social_backend.dto.response.PublicUserProfileResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserResponse;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRedis {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    public UserEntity getFindUserByCustomId(String customId) throws JsonProcessingException {
        String key = "FindUserByCustomId:" + customId;
        String json = (String)redisTemplate.opsForValue().get(key);
        if(json == null) return null;
        return mapper.readValue(json, new TypeReference<UserEntity>(){});
    }
    public void saveFindUserByCustomId(UserEntity user, String customId) throws JsonProcessingException {
        String value = mapper.writeValueAsString(user);
        String key = "FindUserByCustomId:" + customId;
        redisTemplate.opsForValue().set(key, value);
    }

    public PublicUserProfileResponse getUserInfo(String customId) throws JsonProcessingException {
        String key = "UserInfo:" + customId;
        String json = (String)redisTemplate.opsForValue().get(key);
        if(json == null) return null;
        return mapper.readValue(json, new TypeReference<PublicUserProfileResponse>(){});
    }
    public void saveUserInfo(PublicUserProfileResponse user) throws JsonProcessingException {
        String key = "UserInfo:" + user.getCustomId();
        String value = mapper.writeValueAsString(user);
        redisTemplate.opsForValue().set(key, value);
    }

    public UserResponse getCurrentUserInfo(String id) throws JsonProcessingException {
        String key = "MyUserInfo:" + id;
        String json = (String)redisTemplate.opsForValue().get(key);
        if(json == null) return null;
        return mapper.readValue(json, new TypeReference<UserResponse>(){});
    }
    public void saveCurrentUserInfo(UserResponse user) throws JsonProcessingException {
        String key = "MyUserInfo:" + user.getId();
        String value = mapper.writeValueAsString(user);
        redisTemplate.opsForValue().set(key, value);
    }
}
