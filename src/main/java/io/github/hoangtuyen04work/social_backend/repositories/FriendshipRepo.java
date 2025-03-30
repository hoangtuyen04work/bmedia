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
            "WHERE f.sender.id = :sender AND f.receiver.id = :receiver AND f.friendship = :friendship")
    boolean existsBySenderIdAndReceiverIdAndFriendship(@Param("sender") String sender,
                                                   @Param("receiver") String receiver,
                                                   @Param("friendship") Friendship friendship);
    @Query(value = "SELECT  f FROM FriendshipEntity f " +
            "WHERE f.sender.id = :sender AND f.receiver.id = :receiver")
    Optional<FriendshipEntity> findBySenderIdAndReceiverId(@Param("sender") String sender,
                                                       @Param("receiver") String receiver);

    @Query("SELECT f FROM FriendshipEntity f WHERE (f.sender.id = :userId AND f.receiver.id = :friendId) " +
            "OR (f.sender.id = :friendId AND f.receiver.id = :userId)")
    Optional<FriendshipEntity> findByUserIdAndFriendId(@Param("userId") String userId,
                                                       @Param("friendId") String friendId);

    @Query("SELECT f.sender FROM FriendshipEntity f WHERE f.receiver.id = :id AND f.friendship = :friendship")
    Optional<Set<UserEntity>> findByReceiverIdAndFriendship(@Param("id") String id
            , @Param("friendship")Friendship friendShip);
    @Query("SELECT f.sender FROM FriendshipEntity f WHERE f.receiver.id = :id AND f.friendship = :friendship")
    Optional<Set<UserEntity>> findBySenderIdAndFriendship(@Param("id") String id
            , @Param("friendship")Friendship friendShip);

}
