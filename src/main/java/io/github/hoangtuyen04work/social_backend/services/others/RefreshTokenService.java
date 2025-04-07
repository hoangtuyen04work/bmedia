package io.github.hoangtuyen04work.social_backend.services.others;

import io.github.hoangtuyen04work.social_backend.entities.RefreshTokenEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;


public interface RefreshTokenService {
    UserEntity getUserIdByRefreshToken(String refreshToken) throws AppException;

    boolean isValidRefreshToken(String refreshToken);

    void deleteRefreshToken(String refreshToken);

    boolean deleteRefreshTokenByUserId(String id);

    String createRefreshToken();

    RefreshTokenEntity createRefreshTokenEntity(UserEntity user);
}
