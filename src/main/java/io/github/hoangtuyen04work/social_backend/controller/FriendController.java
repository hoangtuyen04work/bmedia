package io.github.hoangtuyen04work.social_backend.controller;

import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friendship")
public class FriendController {
    @Autowired
    private FriendshipService service;


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
