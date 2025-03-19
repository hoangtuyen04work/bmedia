package io.github.hoangtuyen04work.social_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.request.ChangePasswordRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserLoginRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.AuthResponse;
import io.github.hoangtuyen04work.social_backend.entities.Authority;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @PutMapping("/changepassword")
    public ApiResponse<AuthResponse> changePassword(@RequestBody ChangePasswordRequest request) throws AppException, JOSEException, JsonProcessingException {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return null;
        //        return ApiResponse.<AuthResponse>builder()
//                .data(authService.changePassword(request))
//                .build();
    }

    @PostMapping("/signup")
    public ApiResponse<AuthResponse> signup(@RequestBody UserCreationRequest request) throws AppException, JOSEException, JsonProcessingException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.signup(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@RequestBody UserLoginRequest request) throws AppException, JOSEException, JsonProcessingException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.login(request))
                .build();
    }

    @GetMapping("/authentication")
    public ApiResponse<Boolean> authentication(@RequestParam String token) throws AppException, ParseException, JOSEException {
        return ApiResponse.<Boolean>builder()
                .data(authService.authenticateToken(token))
                .build();
    }

    @PutMapping("/logoutt")
    public ApiResponse<Boolean> logout(@RequestBody String token) throws ParseException, AppException {
        return ApiResponse.<Boolean>builder()
                .data(authService.logout(token))
                .build();
    }

    @PutMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(@RequestBody String refreshToken) throws AppException, ParseException, JOSEException, JsonProcessingException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.refreshToken(refreshToken))
                .build();
    }
}
