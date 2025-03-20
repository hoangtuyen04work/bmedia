package io.github.hoangtuyen04work.social_backend.controller;

import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.request.CommentCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.CommentResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService service;

    @PostMapping("/new")
    public ApiResponse<CommentResponse> createComment(@RequestBody CommentCreationRequest request) throws AppException {
        return ApiResponse.<CommentResponse>builder()
                .data(service.createComment(request))
                .build();
    }

    @GetMapping()
    public ApiResponse<CommentResponse> getCommentById(@RequestParam String id) throws AppException {
        return ApiResponse.<CommentResponse>builder()
                .data(service.findCommentById(id))
                .build();
    }

    @DeleteMapping()
    public ApiResponse<Boolean> deleteComment(@RequestParam String id){
        service.deleteCommentById(id);
        return ApiResponse.<Boolean>builder()
                .data(true)
                .build();
    }

    @PutMapping("")
    public ApiResponse<CommentResponse> editComment(@RequestParam String id,
                                                      @RequestBody CommentCreationRequest request) throws AppException {
        return ApiResponse.<CommentResponse>builder()
                .data(service.editComment(id, request))
                .build();
    }
}
