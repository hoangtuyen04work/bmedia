package io.github.hoangtuyen04work.social_backend.services.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.hoangtuyen04work.social_backend.dto.request.UserEditRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserLoginRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PublicUserProfileResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

import java.text.ParseException;

public interface UserService {


    PageResponse<UserSummaryResponse> searchByCustomId(String customId, Integer page, Integer size)
            throws JsonProcessingException;

    UserResponse getCurrentUserInfo() throws AppException, JsonProcessingException;

    PublicUserProfileResponse getUserInfo(String customId) throws AppException, JsonProcessingException;

    boolean deleteUser() throws AppException;

    UserResponse changeInfo(UserEditRequest request) throws AppException, ParseException;

    UserEntity changePassword(UserEntity user, String newPassword);

    boolean isRightPassword(String password) throws AppException;

    UserEntity getUserCurrent() throws AppException;

    UserEntity findUserById(String id) throws AppException;

    UserEntity findUserByCustomId(String customId) throws AppException, JsonProcessingException;

    UserEntity loginByEmail(UserLoginRequest request) throws AppException;

    UserEntity loginByPhone(UserLoginRequest request) throws AppException;

    UserEntity loginByCustomId(UserLoginRequest request) throws AppException;

    boolean existByEmail(String email);

    boolean existByPhone(String phone);

    boolean existByCustomId(String userId);

    UserEntity createUserByCustomId(UserCreationRequest userCreationRequest) throws AppException;

    UserEntity createUserByEmail(UserCreationRequest userCreationRequest) throws AppException;

    boolean checkAttribute(UserCreationRequest request) throws AppException;

    UserEntity createUserByPhone(UserCreationRequest userCreationRequest) throws AppException;
}
