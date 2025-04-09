package io.github.hoangtuyen04work.social_backend.services.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchRedis {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ObjectMapper mapper;

    public PageResponse<UserSummaryResponse> getSearchByCustomId(String customId, Integer page, Integer size)
            throws JsonProcessingException {
        String json = (String)redisTemplate.opsForValue().get("customId:" + customId + "page:" + page + "size:" + size);
        return mapper.readValue(json, new TypeReference<PageResponse<UserSummaryResponse>>(){});
    }
    public void saveSearchByCustomId(PageResponse<UserSummaryResponse> data, String customId, Integer page, Integer size)
            throws JsonProcessingException {
        String key = "customId:" + customId + "page:" + page + "size:" + size;
        String value = mapper.writeValueAsString(data);
        redisTemplate.opsForValue().set(key, value);
    }
    public void clear(){
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
