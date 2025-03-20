package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepo extends JpaRepository<PostEntity, String> {
    Page<PostEntity> findPostByUser(UserEntity user, Pageable pageable);
    Page<PostEntity> findPostByContentContainingIgnoreCase(String keyWord, Pageable pageable);
}
