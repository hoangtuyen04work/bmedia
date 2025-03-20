package io.github.hoangtuyen04work.social_backend.controller;

import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.request.PostCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.PostEditRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PostResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    @Autowired
    private PostService service;

    @GetMapping("/search")
    public ApiResponse<PageResponse<PostResponse>> getPosts(
                                                            @RequestParam (defaultValue = "0" ) Integer page,
                                                            @RequestParam (defaultValue = "10") Integer size,
                                                            @RequestParam (defaultValue = "")String keyWord)
                                                        throws AppException {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .data(service.getPost("", page, size, 3, keyWord))
                .build();
    }

//    @GetMapping("/home")
//    public ApiResponse<PageResponse<PostResponse>> getPostsHome(
//                                                            @RequestParam (defaultValue = "0" ) Integer page,
//                                                            @RequestParam (defaultValue = "10") Integer size) throws AppException {
//        return ApiResponse.<PageResponse<PostResponse>>builder()
//                .data(service.getPost("", page, size, 1, ""))
//                .build();
//    }

    @GetMapping("/{userId}")
    public ApiResponse<PageResponse<PostResponse>> getPostsProfile(@PathVariable String userId,
                                              @RequestParam (defaultValue = "0" ) Integer page,
                                              @RequestParam (defaultValue = "10") Integer size) throws AppException {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .data(service.getPost(userId, page, size, 2, ""))
                .build();
    }

    @PostMapping("/new")
    public ApiResponse<PostResponse> newPost(@ModelAttribute PostCreationRequest request){
        return ApiResponse.<PostResponse>builder()
                .data(service.newPost(request))
                .build();
    }

    @PutMapping("/")
    public ApiResponse<PostResponse> editPost(@PathVariable String postId, @ModelAttribute PostEditRequest request) throws AppException {
        return ApiResponse.<PostResponse>builder()
                .data(service.editPost(postId, request))
                .build();
    }

    @GetMapping("")
    public ApiResponse<PostResponse> getPost(@RequestParam String postId) throws AppException {
        return ApiResponse.<PostResponse>builder()
                .data(service.getPostById(postId))
                .build();
    }

    @DeleteMapping("/delete/{postId}")
    public ApiResponse<Boolean> deletePost(@PathVariable String postId){
        service.deletePost(postId);
        return ApiResponse.<Boolean>builder()
                .data(true)
                .build();
    }
}

