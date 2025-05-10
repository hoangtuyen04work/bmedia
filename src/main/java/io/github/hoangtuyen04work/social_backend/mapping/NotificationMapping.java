package io.github.hoangtuyen04work.social_backend.mapping;

import io.github.hoangtuyen04work.social_backend.dto.response.NotificationResponse;
import io.github.hoangtuyen04work.social_backend.entities.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NotificationMapping {
    public NotificationResponse toNotificationResponse(NotificationEntity entity){
        return NotificationResponse.builder()
                .content(entity.getContent())
                .id(entity.getId())
                .title(entity.getTitle())
                .read(entity.getIsRead())
                .createdAt(LocalDateTime.ofInstant(entity.getCreationDate(), ZoneId.systemDefault()))
                .build();
    }
    public List<NotificationResponse> toNotificationResponse(List<NotificationEntity> entity){
        return entity.stream().map(this::toNotificationResponse).toList();
    }

}
