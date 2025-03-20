package io.github.hoangtuyen04work.social_backend.utils;

import io.github.hoangtuyen04work.social_backend.dto.response.CommentResponse;
import io.github.hoangtuyen04work.social_backend.entities.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapping {
    public CommentResponse toCommentResponse(CommentEntity commentEntity){
        return CommentResponse.builder()
                .content(commentEntity.getContent())
                .imageLink(commentEntity.getImageLink())
                .build();
    }
}
