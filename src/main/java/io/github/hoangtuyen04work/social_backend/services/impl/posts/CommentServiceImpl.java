package io.github.hoangtuyen04work.social_backend.services.impl.posts;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.hoangtuyen04work.social_backend.dto.request.CommentCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.CommentResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.entities.CommentEntity;
import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.CommentRepo;
import io.github.hoangtuyen04work.social_backend.services.posts.CommentService;
import io.github.hoangtuyen04work.social_backend.services.posts.PostService;
import io.github.hoangtuyen04work.social_backend.services.redis.CommentRedis;
import io.github.hoangtuyen04work.social_backend.services.users.UserService;
import io.github.hoangtuyen04work.social_backend.utils.Amazon3SUtils;
import io.github.hoangtuyen04work.social_backend.mapping.CommentMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private CommentRedis redis;

    @Override
    public PageResponse<CommentResponse> getAllComment(String postId, Integer page, Integer size) throws AppException, JsonProcessingException {
        PageResponse<CommentResponse> result = redis.getAllComment(postId, page, size);
        if(result != null) return result;
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        PostEntity post = postService.findById(postId);
        Page<CommentEntity> pag = repo.findByPost(post, pageable);
        result =  PageResponse.<CommentResponse>builder()
                .content(commentMapping.toCommentResponse(pag.getContent()))
                .pageNumber(pag.getNumber())
                .pageSize(pag.getSize())
                .totalElements(pag.getTotalElements())
                .totalPages(pag.getTotalPages())
                .build();
        redis.saveGetAllComment(result, postId, page, size);
        return result;
    }

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
