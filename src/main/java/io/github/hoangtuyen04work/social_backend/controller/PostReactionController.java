package io.github.hoangtuyen04work.social_backend.controller;

import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.request.ReactionPostRequest;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.posts.PostReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reaction")
public class PostReactionController {
    @Autowired
    private PostReactionService service;

    @PostMapping
    public ApiResponse<Boolean> like(@RequestBody ReactionPostRequest request) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(service. reaction(request, true))
                .build();
    }

    @PutMapping
    public ApiResponse<Boolean> unLike(@RequestBody ReactionPostRequest request) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(service.reaction(request, false))
                .build();
    }
}
