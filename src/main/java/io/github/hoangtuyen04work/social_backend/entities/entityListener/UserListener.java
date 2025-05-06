package io.github.hoangtuyen04work.social_backend.entities.entityListener;

import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.utils.ClearRedisUtils;
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
public class UserListener {
    @Autowired
    ClearRedisUtils clear;

    @PostPersist
    public void postPersist(UserEntity user) {
        clear.clearUserInfo(user);
        clear.clearSearch(user, null);
    }

    @PostUpdate
    public void postUpdate(UserEntity user) {
        clear.clearUserInfo(user);
        clear.clearSearch(user, null);
    }

    @PostRemove
    public void postRemove(UserEntity user){
        clear.clearUserInfo(user);
        clear.clearSearch(user, null);
    }
}
