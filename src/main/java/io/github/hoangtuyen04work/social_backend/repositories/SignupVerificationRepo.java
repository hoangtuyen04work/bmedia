package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.SignupVerificationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignupVerificationRepo extends JpaRepository<SignupVerificationEntity, String> {
    Optional<SignupVerificationEntity> findByEmail(String email);
    @Modifying
    @Transactional
    @Query("DELETE FROM SignupVerificationEntity s WHERE s.email = :email")
    int deleteByEmail(@Param("email") String email);
}
