package io.github.hoangtuyen04work.social_backend.services.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRedis {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    public PageResponse<PostResponse> getGetHomePage(String userId, Integer page, Integer size) throws JsonProcessingException, JsonProcessingException {
        String json = (String)redisTemplate.opsForValue().get("userId:" + userId + "page:" + page + "size:" + size);
        return mapper.readValue(json, new TypeReference<PageResponse<PostResponse>>(){});
    }
    public void saveGetHomePage(PageResponse<PostResponse> data,  String userId, Integer page, Integer size) throws JsonProcessingException {
        String key = ("userId:" + userId + "page:" + page + "size:" + size);
        String value = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(key, value);
    }

    public PageResponse<PostResponse> getPost(String customId,Integer page,Integer size,int PAGE,String keyWord)
            throws  JsonProcessingException {
        String key = "customId:" + customId + "page:" + page + "size:" + size + "PAGE:" + page + "keyWord:" + keyWord;
        String json = (String)redisTemplate.opsForValue().get(key);
        return mapper.readValue(json, new TypeReference<PageResponse<PostResponse>>(){});
    }
    public void saveGetPost
            (PageResponse<PostResponse> data, String customId, Integer page, Integer size,int  PAGE,String keyWord)
            throws JsonProcessingException {
        String key = "customId:" + customId + "page:" + page + "size:" + size + "PAGE:" + page + "keyWord:" + keyWord;
        String value = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(key, value);
    }

    public PageResponse<PostResponse> getMyPost(String userId,Integer page,Integer size)
            throws  JsonProcessingException {
        String key = "userId:" + userId + "page:" + page + "size:" + size;
        String json = (String)redisTemplate.opsForValue().get(key);
        return mapper.readValue(json, new TypeReference<PageResponse<PostResponse>>(){});
    }
    public void saveGetMyPost
            (PageResponse<PostResponse> data, String userId, Integer page, Integer size)
            throws JsonProcessingException {
        String key = "userId:" + userId + "page:" + page + "size:" + size ;
        String value = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(key, value);
    }


    public void clear(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }



}
