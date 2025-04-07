package io.github.hoangtuyen04work.social_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.request.ChangePasswordRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserLoginRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.AuthResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.users.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @Transactional
    @PutMapping("/changepassword")
    public ApiResponse<AuthResponse> changePassword(@RequestBody ChangePasswordRequest request) throws JOSEException, AppException {
                return ApiResponse.<AuthResponse>builder()
                .data(authService.changePassword(request))
                .build();
    }

    @PostMapping("/signup")
    public ApiResponse<AuthResponse> signup(@RequestBody UserCreationRequest request) throws AppException, JOSEException, JsonProcessingException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.signup(request))
                .build();
    }

    @Transactional
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

    @Transactional
    @PutMapping("/logoutt")
    public ApiResponse<Boolean> logout(@RequestBody String token) throws ParseException, AppException {
        return ApiResponse.<Boolean>builder()
                .data(authService.logout(token))
                .build();
    }

    @Transactional
    @PutMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(@RequestBody String refreshToken) throws AppException, ParseException, JOSEException, JsonProcessingException {
        return ApiResponse.<AuthResponse>builder()
                .data(authService.refreshToken(refreshToken))
                .build();
    }
}
