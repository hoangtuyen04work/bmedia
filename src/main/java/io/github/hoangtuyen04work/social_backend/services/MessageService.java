package io.github.hoangtuyen04work.social_backend.services;

import io.github.hoangtuyen04work.social_backend.dto.request.MessageCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.MessageResponse;
import io.github.hoangtuyen04work.social_backend.entities.MessageEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

import java.util.List;

public interface MessageService {

    MessageResponse editMessage(String messageId, String newContent) throws AppException;

    void deleteById(String messageId);

    MessageResponse findById(String messageId) throws AppException;

    List<MessageResponse> getMessages(String conversationId, Integer page, Integer size);

    MessageResponse sendMessage(MessageCreationRequest request) throws AppException;

    MessageEntity createMessage(MessageCreationRequest request) throws AppException;
}
