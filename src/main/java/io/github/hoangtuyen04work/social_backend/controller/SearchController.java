package io.github.hoangtuyen04work.social_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PostResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.posts.PostService;
import io.github.hoangtuyen04work.social_backend.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @GetMapping("/user")
    public ApiResponse<PageResponse<UserSummaryResponse>> findByCustomId(@RequestParam String customId,
                                                                         @RequestParam(defaultValue = "0") Integer page,
                                                                         @RequestParam(defaultValue = "10") Integer size) throws JsonProcessingException {
        return ApiResponse.<PageResponse<UserSummaryResponse>>builder()
                .data(userService.searchByCustomId(customId, page, size))
                .build();
    }

    @GetMapping("/post")
    public ApiResponse<PageResponse<PostResponse>> getPosts(
            @RequestParam (defaultValue = "0" ) Integer page,
            @RequestParam (defaultValue = "10") Integer size,
            @RequestParam (defaultValue = "")String keyWord)
            throws AppException, JsonProcessingException {
        return ApiResponse.<PageResponse<PostResponse>>builder()
                .data(postService.getPost("", page, size, 3, keyWord))
                .build();
    }
}
