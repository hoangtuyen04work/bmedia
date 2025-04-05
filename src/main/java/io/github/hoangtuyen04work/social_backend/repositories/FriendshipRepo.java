package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.FriendshipEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
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

    @Query("SELECT f.receiver FROM FriendshipEntity f WHERE f.sender.id = :id AND f.friendship = :friendship")
    Optional<Set<UserEntity>> findBySenderIdAndFriendship(@Param("id") String id
            , @Param("friendship")Friendship friendShip);

//    @Query(value = "SELECT u.id, u.custom_id, u.user_name, u.image_link, c.id, m.content, m.creation_date, m.user_id " +
//            "FROM users u " +
//            "JOIN friendship f " +
//                "ON (f.sender = :userId AND f.receiver = u.id AND f.friendship = 'ACCEPTED') " +
//                "OR (f.receiver = :userId AND f.sender = u.id AND f.friendship = 'ACCEPTED') " +
//            "JOIN conversations c " +
//                "ON (u.id = c.user1_id AND c.user2_id = :userId) " +
//                "OR (u.id = c.user2_id AND c.user1_id = :userId) " +
//            "JOIN (" +
//                "SELECT m.id,  k.id as conversation_id, MAX(m.creation_date) AS creation_date, m.content, m.user_id " +
//                "FROM  messages m " +
//                "RIGHT JOIN " +
//                    "(SELECT c.id " +
//                    "FROM conversations c " +
//                    "WHERE c.user1_id = :userId OR c.user2_id = :userId) k " +
//                    "ON k.id = m.conversation_id " +
//                "GROUP BY m.conversation_id ) m " +
//                "ON m.conversation_id = c.id " +
//            "WHERE u.id != :userId " +
//            "ORDER BY m.creation_date DESC", nativeQuery = true)


    @Query(value = "SELECT \n" +
            "    u.id AS user_id,\n" +
            "    u.custom_id,\n" +
            "    u.user_name,\n" +
            "    u.image_link,\n" +
            "    c.id AS conversation_id,\n" +
            "    m.content AS last_message,\n" +
            "    m.creation_date,\n" +
            "    m.user_id AS sender_id\n" +
            "FROM users u\n" +
            "JOIN friendship f ON (\n" +
            "    (u.id = f.sender AND f.receiver = :userId) OR\n" +
            "    (u.id = f.receiver AND f.sender = :userId)\n" +
            ")\n" +
            "AND f.friendship = 'ACCEPTED'\n" +
            "JOIN conversations c ON \n" +
            "    (c.user1_id = u.id AND c.user2_id = :userId) OR\n" +
            "    (c.user2_id = u.id AND c.user1_id = :userId)\n" +
            "LEFT JOIN (\n" +
            "    SELECT m1.*\n" +
            "    FROM messages m1\n" +
            "    INNER JOIN (\n" +
            "        SELECT conversation_id, MAX(creation_date) AS max_date\n" +
            "        FROM messages\n" +
            "        GROUP BY conversation_id\n" +
            "    ) latest ON m1.conversation_id = latest.conversation_id AND m1.creation_date = latest.max_date\n" +
            ") m ON m.conversation_id = c.id\n" +
            "WHERE u.id != :userId", nativeQuery = true)
    List<Object[]> getAllFriendAndConversation(@Param("userId")String userId);
}
