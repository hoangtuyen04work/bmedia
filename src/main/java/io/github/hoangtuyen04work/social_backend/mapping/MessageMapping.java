package io.github.hoangtuyen04work.social_backend.mapping;

import io.github.hoangtuyen04work.social_backend.dto.response.MessageResponse;
import io.github.hoangtuyen04work.social_backend.entities.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapping {
    public List<MessageResponse> toMessageResponse(List<MessageEntity> messageEntities){
        return messageEntities.stream().map(this::toMessageResponse).toList();
    }
    public MessageResponse toMessageResponse(MessageEntity message){
        return MessageResponse.builder()
                .conversationId(message.getConversation().getId())
                .content(message.getContent())
                .imageLink(message.getImageLink())
                .sendAt(message.getCreationDate())
                .senderId(message.getUser().getId())
                .build();
    }
}
