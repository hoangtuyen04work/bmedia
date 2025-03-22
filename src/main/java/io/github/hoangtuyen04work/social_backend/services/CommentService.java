package io.github.hoangtuyen04work.social_backend.services;

import io.github.hoangtuyen04work.social_backend.dto.request.CommentCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.CommentResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.entities.CommentEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

public interface CommentService {
    PageResponse<CommentResponse> getAllComment(String postId, Integer page, Integer size) throws AppException;

    CommentResponse createComment(CommentCreationRequest request) throws AppException;

    CommentResponse findCommentById(String id) throws AppException;

    void deleteCommentById(String id);

    CommentResponse editComment(String id, CommentCreationRequest request) throws AppException;

    CommentEntity findById(String id) throws AppException;
}
