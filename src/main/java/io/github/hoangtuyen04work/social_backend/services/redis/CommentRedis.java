package io.github.hoangtuyen04work.social_backend.services.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hoangtuyen04work.social_backend.dto.response.CommentResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentRedis {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper mapper;

    public PageResponse<CommentResponse> getAllComment(String postId, Integer page, Integer size)
            throws  JsonProcessingException {
        String json = (String)redisTemplate.opsForValue().get("Comment Of Post Id:" + postId + "page:" + page + "size:" + size);
        if(json == null) return null;
        return mapper.readValue(json, new TypeReference<PageResponse<CommentResponse>>(){});
    }
    public void saveGetAllComment(PageResponse<CommentResponse> data,  String postId, Integer page, Integer size)
            throws JsonProcessingException {
        String key = ("Comment Of Post Id:" + postId + "page:" + page + "size:" + size);
        String value = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(key, value);
    }
}
