package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, String> {
    boolean existsByCustomIdAndState(String userId, State state);
    boolean existsByEmailAndState(String email, State state);
    boolean existsByPhoneAndState(String phone, State state);
    Optional<UserEntity> findByEmailAndState(String email, State state);
    Optional<UserEntity> findByPhoneAndState(String phone, State state);
    Optional<UserEntity> findByCustomIdAndState(String userId, State state);
    Page<UserEntity> findByCustomIdContainingAndState(String customId, State state, Pageable pageable);
}
