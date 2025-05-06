package io.github.hoangtuyen04work.social_backend.entities.entityListener;

import io.github.hoangtuyen04work.social_backend.entities.CommentEntity;
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
public class CommentListener {
    @Autowired
    ClearRedisUtils clear;

    @PostPersist
    public void postPersist(CommentEntity cmt) {
        clear.clearComments(cmt);
    }

    @PostUpdate
    public void postUpdate(CommentEntity cmt) {
        clear.clearComments(cmt);
    }

    @PostRemove
    public void postRemove(CommentEntity cmt){
        clear.clearComments(cmt);
    }
}
