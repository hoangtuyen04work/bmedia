package io.github.hoangtuyen04work.social_backend.services;

import io.github.hoangtuyen04work.social_backend.dto.request.PostCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.PostEditRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PostResponse;
import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PageResponse<PostResponse> getMyPost(Integer page, Integer size) throws AppException;

    // PAGE = 1 -> HOME PAGE
    // PAGE = 2 -> PROFILE PAGE;
    // PAGE = 3 -> SEARCH PAGE
    PageResponse<PostResponse> getPost(String userId, Integer page, Integer size, int PAGE, String keyWord)
            throws AppException;

    Page<PostEntity> getProfilePage(String userId, Pageable pageable) throws AppException;

    Page<PostEntity> getSearchPage(Pageable pageable, String keyWord);

    PostResponse newPost(PostCreationRequest request) throws AppException;

    PostResponse editPost(String postId, PostEditRequest request) throws AppException;

    PostEntity findById(String id) throws AppException;

    PostResponse getPostById(String postId) throws AppException;

    void deletePost(String postId);
}
