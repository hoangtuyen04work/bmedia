package io.github.hoangtuyen04work.social_backend.services.impl.others;

import io.github.hoangtuyen04work.social_backend.dto.response.NotificationResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.entities.NotificationEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.mapping.NotificationMapping;
import io.github.hoangtuyen04work.social_backend.repositories.NotificationRepo;
import io.github.hoangtuyen04work.social_backend.services.others.NotificationService;
import io.github.hoangtuyen04work.social_backend.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepo repo;


    @Autowired
    private NotificationMapping mapping;

    @Override
    public boolean readNotification() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        repo.readNotificationByUserId(userId);
        return true;
    }

    @Override
    public boolean sendNotification(UserEntity receiver, String content, String title) throws AppException {
        repo.save(NotificationEntity.builder()
                .isRead(false)
                .user(receiver)
                .content(content)
                .title(title)
                .build());
        return true;
    }

    @Override
    public PageResponse<NotificationResponse> getAllNotification(Integer page, Integer size){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        Page<NotificationEntity> pag = repo.getAllNotificationByUserId(userId, pageable);
        return PageResponse.<NotificationResponse>builder()
                .content(mapping.toNotificationResponse(pag.getContent()))
                .pageNumber(pag.getNumber())
                .pageSize(pag.getSize())
                .totalElements(pag.getTotalElements())
                .totalPages(pag.getTotalPages())
                .build();
    }
}
