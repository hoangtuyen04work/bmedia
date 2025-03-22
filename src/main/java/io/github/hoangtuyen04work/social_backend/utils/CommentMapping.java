package io.github.hoangtuyen04work.social_backend.utils;

import io.github.hoangtuyen04work.social_backend.dto.response.CommentResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PostResponse;
import io.github.hoangtuyen04work.social_backend.entities.CommentEntity;
import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapping {
    public List<CommentResponse> toCommentResponse(List<CommentEntity> commentEntity){
        return commentEntity.stream().map(this::toCommentResponse).toList();
    }
    public CommentResponse toCommentResponse(CommentEntity commentEntity){
        return CommentResponse.builder()
                .id(commentEntity.getId())
                .imageLink(commentEntity.getImageLink())
                .avatarLink(commentEntity.getUser().getImageLink())
                .userId(commentEntity.getUser().getId())
                .userName(commentEntity.getUser().getUserName())
                .content(commentEntity.getContent())
                .imageLink(commentEntity.getImageLink())
                .build();
    }
}
