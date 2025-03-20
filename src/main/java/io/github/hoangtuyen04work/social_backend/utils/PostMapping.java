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
    public PostResponse toPostResponse(PostEntity post){
        return PostResponse.builder()
                .id(post.getId())
                .content(post.getContent())
                .creationDate(LocalDateTime.from(post.getCreationDate().atZone(ZoneId.systemDefault()).toLocalDate()))
                .modifiedDate(LocalDateTime.from(post.getModifiedDate().atZone(ZoneId.systemDefault()).toLocalDate()))
                .deleteDate(LocalDateTime.from(post.getDeleteDate().atZone(ZoneId.systemDefault()).toLocalDate()))
                .imageLink(post.getImageLink())
                .build();
    }
}
