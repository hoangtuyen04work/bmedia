package io.github.hoangtuyen04work.social_backend.controller;

import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.request.EditUserRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.UserResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.UserService;
import jakarta.transaction.Transactional;
import jdk.jfr.TransitionTo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @Transactional
    @PutMapping("/user/edit")
    public ApiResponse<UserResponse> editUserInfo(@ModelAttribute EditUserRequest request) throws AppException {
        return ApiResponse.<UserResponse>builder()
                .data(userService.changeInfo(request))
                .build();
    }

    @Transactional
    @PutMapping("/user/delete")
    public ApiResponse<Boolean> deleteUser() throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(userService.deleteUser())
                .build();
    }
}
