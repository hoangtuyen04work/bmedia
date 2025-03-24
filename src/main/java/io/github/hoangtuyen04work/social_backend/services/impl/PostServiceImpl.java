package io.github.hoangtuyen04work.social_backend.services.impl;

import io.github.hoangtuyen04work.social_backend.dto.request.PostCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.PostEditRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PostResponse;
import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.PostRepo;
import io.github.hoangtuyen04work.social_backend.services.PostService;
import io.github.hoangtuyen04work.social_backend.services.UserService;
import io.github.hoangtuyen04work.social_backend.utils.Amazon3SUtils;
import io.github.hoangtuyen04work.social_backend.utils.PostMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepo repo;
    @Autowired
    private PostMapping postMapping;
    @Autowired
    private Amazon3SUtils amazon3SUtils;
    @Autowired
    private UserService userService;

    @Override
    public PageResponse<PostResponse> getMyPost(Integer page, Integer size) throws AppException {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        UserEntity user = userService.getUserCurrent();
        Page<PostEntity> pag = repo.findPostByUser(user, pageable);
        return PageResponse.<PostResponse>builder()
                .content(postMapping.toPostResponse(pag.getContent()))
                .pageNumber(pag.getNumber())
                .pageSize(pag.getSize())
                .totalElements(pag.getTotalElements())
                .totalPages(pag.getTotalPages())
                .build();
    }

    // PAGE = 1 -> HOME PAGE
    // PAGE = 2 -> PROFILE PAGE;
    // PAGE = 3 -> SEARCH PAGE
    @Override
    public PageResponse<PostResponse> getPost(String customId, Integer page, Integer size, int PAGE, String keyWord) throws AppException {
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        Page<PostEntity> pag = null;
        if(PAGE == 3) pag = getSearchPage(pageable, keyWord);
        else if(PAGE == 2) pag = getProfilePageByCustomId(customId, pageable);
//        else if(PAGE == 1) pag = getHomePage(userId, pageable);
        assert pag != null;
        return PageResponse.<PostResponse>builder()
                .content(postMapping.toPostResponse(pag.getContent()))
                .pageNumber(pag.getNumber())
                .pageSize(pag.getSize())
                .totalElements(pag.getTotalElements())
                .totalPages(pag.getTotalPages())
                .build();
    }

//    @Override
//    public Page<PostEntity> getHomePage(String userId, Pageable pageable) throws AppException {
//        UserEntity user  = userService.findUserById(userId);
//        Page<PostEntity> pag = repo.findPostByUser(user, pageable);
//    }

    @Override
    public Page<PostEntity> getProfilePageByCustomId(String customId, Pageable pageable) throws AppException {
        UserEntity user  = userService.findUserByCustomId(customId);
        return repo.findPostByUser(user, pageable);
    }

    @Override
    public Page<PostEntity> getProfilePageByUserId(String userId, Pageable pageable) throws AppException {
        UserEntity user  = userService.findUserById(userId);
        return repo.findPostByUser(user, pageable);
    }

    @Override
    public Page<PostEntity> getSearchPage(Pageable pageable, String keyWord){
        return repo.findPostByContentContainingIgnoreCase(keyWord, pageable);
    }

    @Override
    public PostResponse newPost(PostCreationRequest request) throws AppException {
        PostEntity post = postMapping.toPostEntity(request);
        post.setUser(userService.getUserCurrent());
        if(request.getImageFile() != null)
            post.setImageLink(amazon3SUtils.addImageS3(request.getImageFile()));
        return postMapping.toPostResponse(repo.save(post));
    }

    @Override
    public PostResponse editPost(String postId, PostEditRequest request) throws AppException {
        PostEntity post = findById(postId);
        post.setContent(request.getContent());
        if(request.getImageFile() == null) post.setImageLink("");
        post.setImageLink(amazon3SUtils.addImageS3(request.getImageFile()));
        return postMapping.toPostResponse(post);
    }

    @Override
    public PostEntity findById(String id) throws AppException {
        return repo.findById(id).orElseThrow(()-> new AppException(ErrorCode.CONFLICT));
    }

    @Override
    public PostResponse getPostById(String postId) throws AppException {
        return postMapping.toPostResponse(findById(postId));
    }

    @Override
    public void deletePost(String postId){
        repo.deleteById(postId);
    }
}
