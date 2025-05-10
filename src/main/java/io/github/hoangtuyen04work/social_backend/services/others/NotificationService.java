package io.github.hoangtuyen04work.social_backend.services.others;

import io.github.hoangtuyen04work.social_backend.dto.response.NotificationResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

public interface NotificationService {
    boolean readNotification();

    boolean sendNotification(UserEntity receiver, String content, String title) throws AppException;

    PageResponse<NotificationResponse> getAllNotification(Integer page, Integer size);
}
