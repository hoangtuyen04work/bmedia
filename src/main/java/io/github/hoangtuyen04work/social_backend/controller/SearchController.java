package io.github.hoangtuyen04work.social_backend.controller;

import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
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
                                                                         @RequestParam(defaultValue = "10") Integer size)
    {
        return ApiResponse.<PageResponse<UserSummaryResponse>>builder()
                .data(userService.searchByCustomId(customId, page, size))
                .build();
    }
}
