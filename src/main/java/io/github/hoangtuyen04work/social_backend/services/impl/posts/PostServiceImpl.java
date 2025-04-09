package io.github.hoangtuyen04work.social_backend.services.impl.posts;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.hoangtuyen04work.social_backend.dto.request.PostCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.PostEditRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PostResponse;
import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.PostRepo;
import io.github.hoangtuyen04work.social_backend.services.redis.PostRedis;
import io.github.hoangtuyen04work.social_backend.services.users.FriendshipService;
import io.github.hoangtuyen04work.social_backend.services.posts.PostService;
import io.github.hoangtuyen04work.social_backend.services.users.UserService;
import io.github.hoangtuyen04work.social_backend.utils.Amazon3SUtils;
import io.github.hoangtuyen04work.social_backend.mapping.PostMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

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
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private PostRedis redis;

    @Override
    public PageResponse<PostResponse> getHomePage(Integer page, Integer size) throws JsonProcessingException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        PageResponse<PostResponse> response = redis.getGetHomePage(userId, page, size);
        if(response != null) return response;
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        Set<UserEntity> friends = friendshipService.getMyFriend2();
        Page<PostEntity> pag = repo.findByUsers(friends, pageable);
        response =  PageResponse.<PostResponse>builder()
                .content(postMapping.toPostResponse(pag.getContent()))
                .pageNumber(pag.getNumber())
                .pageSize(pag.getSize())
                .totalElements(pag.getTotalElements())
                .totalPages(pag.getTotalPages())
                .build();
        redis.saveGetHomePage(response, userId, page, size);
        return response;
    }
    @Override
    public PageResponse<PostResponse> getMyPost(Integer page, Integer size) throws AppException, JsonProcessingException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        PageResponse<PostResponse> result = redis.getMyPost(userId, page, size);
        if(result != null) return result;
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        UserEntity user = userService.getUserCurrent();
        Page<PostEntity> pag = repo.findPostByUser(user, pageable);
        result  = PageResponse.<PostResponse>builder()
                .content(postMapping.toPostResponse(pag.getContent()))
                .pageNumber(pag.getNumber())
                .pageSize(pag.getSize())
                .totalElements(pag.getTotalElements())
                .totalPages(pag.getTotalPages())
                .build();
        redis.saveGetMyPost(result, userId, page, size);
        return result;
    }

    // PAGE = 1 -> HOME PAGE
    // PAGE = 2 -> PROFILE PAGE;
    // PAGE = 3 -> SEARCH PAGE
    @Override
    public PageResponse<PostResponse> getPost(String customId, Integer page, Integer size, int PAGE, String keyWord)
            throws AppException, JsonProcessingException {
        PageResponse<PostResponse> result = redis.getPost(customId, page, size, PAGE, keyWord);
        if(result != null) return result;
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        Page<PostEntity> pag = null;
        if(PAGE == 3) pag = getSearchPage(pageable, keyWord);
        else if(PAGE == 2) pag = getProfilePageByCustomId(customId, pageable);
//        else if(PAGE == 1) pag = getHomePage(userId, pageable);
        assert pag != null;
        result =  PageResponse.<PostResponse>builder()
                .content(postMapping.toPostResponse(pag.getContent()))
                .pageNumber(pag.getNumber())
                .pageSize(pag.getSize())
                .totalElements(pag.getTotalElements())
                .totalPages(pag.getTotalPages())
                .build();
        redis.saveGetPost(result, customId, page, size, PAGE, keyWord);
        return result;
    }

//    @Override
//    public Page<PostEntity> getHomePage(String userId, Pageable pageable) throws AppException {
//        UserEntity user  = userService.findUserById(userId);
//        Page<PostEntity> pag = repo.findPostByUser(user, pageable);
//    }
    @Override
    public Page<PostEntity> getProfilePageByCustomId(String customId, Pageable pageable)
            throws AppException, JsonProcessingException {
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
