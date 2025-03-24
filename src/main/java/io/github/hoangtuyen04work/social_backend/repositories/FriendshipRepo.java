package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.FriendshipEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FriendshipRepo extends JpaRepository<FriendshipEntity, String> {
    @Query(value = "SELECT  COUNT(f) > 0 FROM FriendshipEntity f " +
            "WHERE f.user1.id = :userId AND f.user2.id = :friendId AND f.friendship = :friendship")
    boolean existsByUserIdAndFriendIdAndFriendship(@Param("userId") String userId,
                                                   @Param("friendId") String friendId,
                                                   @Param("friendship") Friendship friendship);
    @Query(value = "SELECT  f FROM FriendshipEntity f " +
            "WHERE f.user1.id = :userId AND f.user2.id = :friendId")
    Optional<FriendshipEntity> findByUserIdAndFriendId(@Param("userId") String userId,
                                                       @Param("friendId") String friendId);
    @Query("SELECT f.user1 FROM FriendshipEntity  f WHERE f.user2.id = :id")
    Optional<Set<UserEntity>> findByUser2Id(@Param("id") String id);
    @Query("SELECT f.user2 FROM FriendshipEntity f WHERE f.user1.id = :id")
    Optional<Set<UserEntity>> findByUser1Id(@Param("id") String id);

}
