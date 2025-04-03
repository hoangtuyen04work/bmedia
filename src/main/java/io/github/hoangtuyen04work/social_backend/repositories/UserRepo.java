package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.Friendship;
import io.github.hoangtuyen04work.social_backend.enums.State;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT u.id, u.custom_id, u.user_name, u.image_link, " +
            "CASE " +
                "WHEN f.friendship IS NOT NULL AND u.id = f.sender AND f.friendship = 'PENDING' THEN 'WAITING' " +
                "WHEN f.friendship IS NOT NULL THEN f.friendship " +
                "ELSE NULL " +
            "END AS friendship " +
            "FROM users u LEFT JOIN friendship f " +
            "ON (u.id = f.sender AND f.receiver = :userId) " +
            "OR (u.id = f.receiver AND f.sender = :userId) " +
            "WHERE  u.custom_id REGEXP :regexp", nativeQuery = true)
    Page<Object[]> findFriendByCustomIdContainingAndState(@Param("regexp") String regexp,
                                                                     @Param("userId") String userId,
                                                                     @Param("state") State state,
                                                                     Pageable pageable);

}
