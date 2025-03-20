package io.github.hoangtuyen04work.social_backend.services.impl;

import io.github.hoangtuyen04work.social_backend.dto.request.CommentCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.CommentResponse;
import io.github.hoangtuyen04work.social_backend.entities.CommentEntity;
import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.CommentRepo;
import io.github.hoangtuyen04work.social_backend.services.CommentService;
import io.github.hoangtuyen04work.social_backend.services.PostService;
import io.github.hoangtuyen04work.social_backend.services.UserService;
import io.github.hoangtuyen04work.social_backend.utils.Amazon3SUtils;
import io.github.hoangtuyen04work.social_backend.utils.CommentMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepo repo;
    @Autowired
    private Amazon3SUtils amazon3SUtils;
    @Autowired
    private CommentMapping commentMapping;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @Override
    public CommentResponse createComment(CommentCreationRequest request) throws AppException {
        UserEntity user = userService.getUserCurrent();
        PostEntity post = postService.findById(request.getPostId());
        CommentEntity commentEntity = CommentEntity.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .imageLink(request.getImageFile() == null ? "" : amazon3SUtils.addImageS3(request.getImageFile()))
                .build();
        repo.save(commentEntity);
        return commentMapping.toCommentResponse(commentEntity);
    }

    @Override
    public CommentResponse findCommentById(String id) throws AppException {
        return commentMapping.toCommentResponse(findById(id));
    }

    @Override
    public void deleteCommentById(String id){
        repo.deleteById(id);
    }

    @Override
    public CommentResponse editComment(String id, CommentCreationRequest request) throws AppException {
        CommentEntity commentEntity = findById(id);
        commentEntity.setContent(request.getContent());
        if(!request.getImageFile().isEmpty())
            commentEntity.setImageLink(amazon3SUtils.addImageS3(request.getImageFile()));
        else commentEntity.setImageLink("");
        repo.save(commentEntity);
        return commentMapping.toCommentResponse(commentEntity);
    }

    @Override
    public CommentEntity findById(String id) throws AppException {
        return repo.findById(id).orElseThrow(()-> new AppException(ErrorCode.CONFLICT));
    }
}
