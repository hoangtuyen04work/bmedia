package io.github.hoangtuyen04work.social_backend.repositories;

import io.github.hoangtuyen04work.social_backend.entities.NotificationEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationEntity, String> {
    @Transactional
    @Modifying
    @Query("UPDATE NotificationEntity n SET n.isRead = true WHERE n.user.id = :userId")
    void readNotificationByUserId(@Param("userId") String userId);

    @Query("SELECT n FROM NotificationEntity n WHERE n.user.id = :userId")
    Page<NotificationEntity> getAllNotificationByUserId(@Param("userId") String userId, Pageable pageable);
}

