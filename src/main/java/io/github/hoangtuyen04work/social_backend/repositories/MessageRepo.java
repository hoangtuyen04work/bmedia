package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.MessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepo extends JpaRepository<MessageEntity, String> {
    @Query(value = "SELECT m FROM MessageEntity m WHERE m.conversation.id = :conversationId")
    Optional<List<MessageEntity>> findByConversationId(@Param("conversationId") String conversationId, Pageable pageable);
}
