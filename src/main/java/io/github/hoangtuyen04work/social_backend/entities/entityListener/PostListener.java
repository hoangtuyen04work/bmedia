package io.github.hoangtuyen04work.social_backend.entities.entityListener;

import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
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
public class PostListener {
    @Autowired
    ClearRedisUtils clear;

    @PostPersist
    public void postPersist(PostEntity post) {
        clear.clearPostInfo(post.getUser());
        clear.clearSearch(null, post);
    }

    @PostUpdate
    public void postUpdate(PostEntity post) {
        clear.clearPostInfo(post.getUser());
        clear.clearSearch(null, post);
    }

    @PostRemove
    public void postRemove(PostEntity post){
        clear.clearPostInfo(post.getUser());
        clear.clearSearch(null, post);
    }
}
