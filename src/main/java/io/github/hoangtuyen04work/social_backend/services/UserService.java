package io.github.hoangtuyen04work.social_backend.services;

import io.github.hoangtuyen04work.social_backend.dto.request.ChangePasswordRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.EditUserRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserLoginRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.UserResponse;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

public interface UserService {

    boolean deleteUser() throws AppException;

    UserResponse changeInfo(EditUserRequest request) throws AppException;

    UserEntity changePassword(UserEntity user, String newPassword);

    boolean isRightPassword(String password) throws AppException;

    UserEntity getUserCurrent() throws AppException;

    UserEntity findUserById(String id) throws AppException;

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
