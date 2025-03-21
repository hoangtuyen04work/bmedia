package io.github.hoangtuyen04work.social_backend.utils;

import io.github.hoangtuyen04work.social_backend.dto.response.MessageResponse;
import io.github.hoangtuyen04work.social_backend.entities.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapping {
    public List<MessageResponse> toMessageResponse(List<MessageEntity> messageEntities){
        return messageEntities.stream().map(this::toMessageResponse).toList();
    }
    public MessageResponse toMessageResponse(MessageEntity message){
        return MessageResponse.builder()
                .content(message.getContent())
                .imageLink(message.getImageLink())
                .sendAt(LocalDateTime.from(message.getCreationDate()))
                .senderId(message.getUser().getId())
                .build();
    }
}
