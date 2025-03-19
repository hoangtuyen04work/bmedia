package io.github.hoangtuyen04work.social_backend.services.impl;

import io.github.hoangtuyen04work.social_backend.entities.RefreshTokenEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.RefreshTokenRepo;
import io.github.hoangtuyen04work.social_backend.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private RefreshTokenRepo repo;

    @Override
    public UserEntity getUserIdByRefreshToken(String refreshToken) throws AppException {
        Optional<RefreshTokenEntity> x = repo.findByRefreshToken(refreshToken);
        return repo.findByRefreshToken(refreshToken).orElseThrow(() -> new AppException(ErrorCode.CONFLICT)).getUser();
    }

    @Override
    public boolean isValidRefreshToken(String refreshToken){
        return repo.existsByRefreshToken(refreshToken);
    }

    @Override
    public void deleteRefreshToken(String refreshToken){
        repo.deleteByRefreshToken(refreshToken);
    }

    @Override
    public boolean deleteRefreshTokenByUserId(String id){
        repo.deleteRefreshTokenEntityByUserId(id);
        return true;
    }

    @Override
    public String createRefreshToken(){
        return UUID.randomUUID().toString();
    }

    @Override
    public RefreshTokenEntity createRefreshTokenEntity(UserEntity user){
        RefreshTokenEntity refreshTokenEntity =  RefreshTokenEntity.builder()
                .expirationTime(Date.from(Instant.now().plus(24*60*60, ChronoUnit.SECONDS)))
                .refreshToken(createRefreshToken())
                .user(user)
                .build();
        return repo.save(refreshTokenEntity);
    }
}
