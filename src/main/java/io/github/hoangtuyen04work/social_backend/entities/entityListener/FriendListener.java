package io.github.hoangtuyen04work.social_backend.entities.entityListener;

import io.github.hoangtuyen04work.social_backend.entities.CommentEntity;
import io.github.hoangtuyen04work.social_backend.entities.FriendshipEntity;
import io.github.hoangtuyen04work.social_backend.services.redis.CommentRedis;
import io.github.hoangtuyen04work.social_backend.services.redis.FriendRedis;
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
public class FriendListener {
    @Autowired
    FriendRedis redis;

    @PostPersist
    public void postPersist(FriendshipEntity user) {
        redis.clear();
    }

    @PostUpdate
    public void postUpdate(FriendshipEntity user) {
        redis.clear();
    }

    @PostRemove
    public void postRemove(FriendshipEntity user){
        redis.clear();
    }
}
