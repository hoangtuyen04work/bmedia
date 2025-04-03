package io.github.hoangtuyen04work.social_backend.services;

import io.github.hoangtuyen04work.social_backend.entities.ConversationEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

public interface ConversationService {

    boolean existConversation(String conservationId);

    ConversationEntity findById(String id) throws AppException;

    ConversationEntity createConversation(UserEntity user1, UserEntity user2);
}
