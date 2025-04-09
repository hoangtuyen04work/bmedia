package io.github.hoangtuyen04work.social_backend.entities.entityListener;

import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.services.redis.PostRedis;
import io.github.hoangtuyen04work.social_backend.services.redis.SearchRedis;
import io.github.hoangtuyen04work.social_backend.services.redis.UserRedis;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Builder
@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostListener {
    @Autowired
    PostRedis redis;
    @Autowired
    SearchRedis searchRedis;

    @PostPersist
    public void postPersist(PostEntity user) {
        redis.clear();
        searchRedis.clear();
    }

    @PostUpdate
    public void postUpdate(PostEntity user) {
        redis.clear();
        searchRedis.clear();
    }

    @PostRemove
    public void postRemove(PostEntity user){
        redis.clear();
        searchRedis.clear();
    }
}
