package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.RefreshTokenEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshTokenEntity, String> {
    @Modifying
    @Transactional
    @Query("DELETE  FROM RefreshTokenEntity r WHERE r.user.id = :id")
    void deleteRefreshTokenEntityByUserId(String id);
    boolean existsByRefreshToken(String refreshToken);
    @Modifying
    @Transactional
    @Query("DELETE  FROM RefreshTokenEntity r WHERE r.refreshToken = :refreshToken")
    void deleteByRefreshToken(String refreshToken);
    @Query( "SELECT r FROM RefreshTokenEntity  r WHERE r.refreshToken = :refreshToken")
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
}
