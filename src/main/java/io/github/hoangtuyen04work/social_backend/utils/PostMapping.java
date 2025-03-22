package io.github.hoangtuyen04work.social_backend.utils;

import io.github.hoangtuyen04work.social_backend.dto.request.PostCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.PostResponse;
import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostMapping {

    public PostEntity toPostEntity(PostCreationRequest request){
        PostEntity post = new PostEntity();
        post.setContent(request.getContent());
        return post;
    }

    public List<PostResponse> toPostResponse(List<PostEntity> posts){
        return posts.stream().map(this::toPostResponse).toList();
    }

    public PostResponse toPostResponse(PostEntity post) {
        return PostResponse.builder()
                .userId(post.getUser().getId())
                .customId(post.getUser().getCustomId())
                .avatarLink(post.getUser().getImageLink() != null ? post.getImageLink() : "")
                .userName(post.getUser().getUserName())
                .id(post.getId() != null ? post.getId() : "")
                .content(post.getContent() != null ? post.getContent() : "")
                .imageLink(post.getImageLink() != null ? post.getImageLink() : "")
                .creationDate(post.getCreationDate() != null ?
                        LocalDate.ofInstant(post.getCreationDate(), ZoneId.systemDefault()) : null)
                .modifiedDate(post.getModifiedDate() != null ?
                        LocalDate.ofInstant(post.getModifiedDate(), ZoneId.systemDefault()) : null)
                .deleteDate(post.getDeleteDate() != null ?
                        LocalDate.ofInstant(post.getDeleteDate(), ZoneId.systemDefault()) : null)
                .build();
    }
}
