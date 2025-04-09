package io.github.hoangtuyen04work.social_backend.services.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hoangtuyen04work.social_backend.dto.response.CommentResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.FriendSummaryResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendRedis {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    public Set<FriendSummaryResponse> getMyFriend(String myId)
            throws  JsonProcessingException {
        String json = (String)redisTemplate.opsForValue().get("myFriend:" + myId);
        return mapper.readValue(json, new TypeReference<Set<FriendSummaryResponse>>(){});
    }
    public void saveGetMyFriend(Set<FriendSummaryResponse> data, String myId)
            throws JsonProcessingException {
        String key = ("myFriend:" + myId);
        String value = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(key, value);
    }

    public PageResponse<UserSummaryResponse> getAllAccepted(String myId, Integer page, Integer size)
            throws  JsonProcessingException {
        String json = (String)redisTemplate.opsForValue().get("Accepted of:" + myId + "page:" + page + "size:" + size);
        return mapper.readValue(json, new TypeReference<PageResponse<UserSummaryResponse>>(){});
    }
    public void saveGetAllAccepted(PageResponse<UserSummaryResponse> data, String myId, Integer page, Integer size)
            throws JsonProcessingException {
        String key = ("Accepted of:" + myId + "page:" + page + "size:" + size);
        String value = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(key, value);
    }

    public PageResponse<UserSummaryResponse> getAllPending(String myId, Integer page, Integer size)
            throws  JsonProcessingException {
        String json = (String)redisTemplate.opsForValue().get("Pending of:" + myId + "page:" + page + "size:" + size);
        return mapper.readValue(json, new TypeReference<PageResponse<UserSummaryResponse>>(){});
    }
    public void saveGetAllPending(PageResponse<UserSummaryResponse> data, String myId, Integer page, Integer size)
            throws JsonProcessingException {
        String key = ("Pending of:" + myId + "page:" + page + "size:" + size);
        String value = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(key, value);
    }

    public PageResponse<UserSummaryResponse> getAllWaiting(String myId, Integer page, Integer size)
            throws  JsonProcessingException {
        String json = (String)redisTemplate.opsForValue().get("Waiting of:" + myId + "page:" + page + "size:" + size);
        return mapper.readValue(json, new TypeReference<PageResponse<UserSummaryResponse>>(){});
    }
    public void saveGetAllWaiting(PageResponse<UserSummaryResponse> data, String myId, Integer page, Integer size)
            throws JsonProcessingException {
        String key = ("Waiting of:" + myId + "page:" + page + "size:" + size);
        String value = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(key, value);
    }




    public void clear(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }



}
