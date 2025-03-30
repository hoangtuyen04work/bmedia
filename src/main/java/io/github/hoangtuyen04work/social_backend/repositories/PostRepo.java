package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PostRepo extends JpaRepository<PostEntity, String> {
    Page<PostEntity> findPostByUser(UserEntity user, Pageable pageable);
    Page<PostEntity> findPostByContentContainingIgnoreCase(String keyWord, Pageable pageable);
    @Query("SELECT p FROM PostEntity p WHERE p.user in :users")
    Page<PostEntity> findByUsers(Set<UserEntity> users, Pageable pageable);
}
