package io.github.hoangtuyen04work.social_backend.services.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import io.github.hoangtuyen04work.social_backend.dto.request.ChangePasswordRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserLoginRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.AuthResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import jakarta.mail.MessagingException;

import java.text.ParseException;

public interface AuthService {

    AuthResponse changePassword(ChangePasswordRequest request) throws JOSEException, AppException;

    AuthResponse refreshToken(String refreshToken) throws AppException, ParseException, JOSEException, JsonProcessingException;

    boolean logout(String token) throws ParseException, AppException;

    boolean authenticateToken(String token) throws AppException, ParseException, JOSEException;

    String generateSignupVerificationCode();

    boolean signupByEmail(UserCreationRequest userCreationRequest) throws MessagingException, AppException;

    AuthResponse verifySignupByEmail(String email, String code, String customId) throws AppException, JOSEException;

    boolean verifyEmailCode(String email, String code) throws AppException;

    AuthResponse signup(UserCreationRequest userCreationRequest)
            throws AppException, JOSEException, JsonProcessingException, MessagingException;

    AuthResponse login(UserLoginRequest request) throws AppException, JOSEException, JsonProcessingException;
}
