package io.github.hoangtuyen04work.social_backend.entities.entityListener;

import io.github.hoangtuyen04work.social_backend.entities.CommentEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.services.redis.CommentRedis;
import io.github.hoangtuyen04work.social_backend.services.redis.PostRedis;
import io.github.hoangtuyen04work.social_backend.services.redis.SearchRedis;
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
public class CommentListener {
    @Autowired
    CommentRedis redis;

    @PostPersist
    public void postPersist(CommentEntity user) {
        redis.clear();
    }

    @PostUpdate
    public void postUpdate(CommentEntity user) {
        redis.clear();
    }

    @PostRemove
    public void postRemove(CommentEntity user){
        redis.clear();
    }
}
