package io.github.hoangtuyen04work.social_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.FriendSummaryResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.users.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendship")
public class FriendController {
    @Autowired
    private FriendshipService service;

    @GetMapping("/all/messing")
    public ApiResponse<Set<FriendSummaryResponse>> getMyFriend() throws AppException, JsonProcessingException {
        return ApiResponse.<Set<FriendSummaryResponse>>builder()
                .data(service.getMyFriend())
                .build();
    }

    @GetMapping("/all/accepted")
    public ApiResponse<PageResponse<UserSummaryResponse>> getAllAccepted
            (@RequestParam(defaultValue = "0") Integer page,
             @RequestParam(defaultValue = "10") Integer size) throws JsonProcessingException {
        return ApiResponse.<PageResponse<UserSummaryResponse>>builder()
                .data(service.getAllAccepted(page, size))
                .build();
    }

    @GetMapping("/all/pending")
    public ApiResponse<PageResponse<UserSummaryResponse>> getAllPending
            (@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) throws JsonProcessingException {
        return ApiResponse.<PageResponse<UserSummaryResponse>>builder()
                .data(service.getAllPending(page, size))
                .build();
    }

    @GetMapping("/all/waiting")
    public ApiResponse<PageResponse<UserSummaryResponse>> getAllWaiting
            (@RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) throws JsonProcessingException {
        return ApiResponse.<PageResponse<UserSummaryResponse>>builder()
                .data(service.getAllWaiting(page, size))
                .build();
    }

    @PostMapping("/add")
    public ApiResponse<Boolean> addFriend(@RequestBody String friendId) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(service.changeFriendShip(friendId, 1))
                .build();
    }

    @PutMapping("/accept")
    public ApiResponse<Boolean> acceptFriend(@RequestBody String friendId) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(service.changeFriendShip(friendId, 2))
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<Boolean> deleteFriend(@RequestBody String friendId) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(service.changeFriendShip(friendId, 3))
                .build();
    }

}
