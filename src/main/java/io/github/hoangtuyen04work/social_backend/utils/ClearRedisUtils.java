package io.github.hoangtuyen04work.social_backend.utils;

import io.github.hoangtuyen04work.social_backend.entities.CommentEntity;
import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
public class ClearRedisUtils {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void clearUserInfo(UserEntity user) {
        String[] patterns = {
                "FindUserByCustomId:" + user.getCustomId(),
                "UserInfo:" + user.getCustomId(),
                "MyUserInfo:" + user.getId()
        };

        for (String pattern : patterns) {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }

    public void clearPostInfo(UserEntity user) {
        String[] patterns = {
                "MyPost userId:" + user.getId() + "*",
                "Post of customId:" + user.getCustomId() + "*",
        };
        for (String pattern : patterns) {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }

    public void clearComments(CommentEntity commentEntity){
        String pattern = "Comment Of Post Id:" + commentEntity.getPost().getId() + "*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    public void clearSearch(UserEntity user, PostEntity post) {
        if(user!= null){
            try {
                String pattern = "Search customId:" + "*" + user.getCustomId() + "*page:*size:*";
                Set<String> keys = redisTemplate.keys(pattern);
                if (keys != null && !keys.isEmpty()) {
                    redisTemplate.delete(keys);
                }
            } catch (Exception e) {
                System.err.println("Failed to clear search caches for user " + user.getCustomId() + ": " + e.getMessage());
            }
        }
        // currently develop
//        else{
//            try {
//                // Pattern for search-related keys containing the customId as a substring
//                String pattern = "Search customId:" + "*" + user.getCustomId() + "*page:*size:*";
//                Set<String> keys = redisTemplate.keys(pattern);
//                if (keys != null && !keys.isEmpty()) {
//                    redisTemplate.delete(keys);
//                }
//            } catch (Exception e) {
//                // Log the error (use a logging framework like SLF4J)
//                System.err.println("Failed to clear search caches for user " + user.getCustomId() + ": " + e.getMessage());
//            }
//        }
    }
}
