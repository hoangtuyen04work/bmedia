package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.PostReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactionRepo extends JpaRepository<PostReactionEntity, String> {
    @Modifying
    @Query("DELETE FROM PostReactionEntity p WHERE p.post.id = :postId AND p.user.id = :userId")
    void deleteByUserIdAndPostId(@Param("userId") String userId, @Param("postId") String postId);
}