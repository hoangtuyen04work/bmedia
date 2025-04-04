package io.github.hoangtuyen04work.social_backend.services.impl;

import io.github.hoangtuyen04work.social_backend.dto.request.MessageCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.MessageResponse;
import io.github.hoangtuyen04work.social_backend.entities.ConversationEntity;
import io.github.hoangtuyen04work.social_backend.entities.MessageEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.MessageRepo;
import io.github.hoangtuyen04work.social_backend.services.ConversationService;
import io.github.hoangtuyen04work.social_backend.services.MessageService;
import io.github.hoangtuyen04work.social_backend.services.UserService;
import io.github.hoangtuyen04work.social_backend.utils.Amazon3SUtils;
import io.github.hoangtuyen04work.social_backend.utils.Base64ToMultipartFile;
import io.github.hoangtuyen04work.social_backend.utils.MessageMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepo repo;
    @Autowired
    private UserService userService;
    @Autowired
    private ConversationService conversationService;
    @Autowired
    private Amazon3SUtils amazon3SUtils;
    @Autowired
    private MessageMapping messageMapping;


    @Override
    public MessageResponse editMessage(String messageId, String newContent) throws AppException {
        MessageEntity message = repo.findById(messageId).orElseThrow( () -> new AppException(ErrorCode.CONFLICT));
        message.setContent(newContent);
        return messageMapping.toMessageResponse(repo.save(message));
    }

    @Override
    public void deleteById(String messageId){
        repo.deleteById(messageId);
    }

    @Override
    public MessageResponse findById(String messageId) throws AppException {
        return messageMapping.toMessageResponse(repo.findById(messageId)
                .orElseThrow(() -> new AppException(ErrorCode.CONFLICT)));
    }

    @Override
    public List<MessageResponse> getMessages(String conversationId, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("creationDate").descending());
        return messageMapping.toMessageResponse(repo.findByConversationId(conversationId, pageable).get());
    }

    @Override
    public MessageResponse sendMessage(MessageCreationRequest request) throws AppException, IOException {
        MessageEntity message = createMessage(request);
        return messageMapping.toMessageResponse(message);
    }

    @Override
    public MessageEntity createMessage(MessageCreationRequest request) throws AppException, IOException {
        ConversationEntity conversation = conversationService.findById(request.getConversationId());
        UserEntity user = userService.findUserById(request.getSenderId());
        String imageLink = null;
        if(request.getImageBase64() != null && !request.getImageBase64().isEmpty()){
            imageLink = amazon3SUtils.addImageS3(Base64ToMultipartFile.convertBase64ToMultipart(request.getImageBase64(), request.getFileName()));
        }
        MessageEntity message = MessageEntity.builder()
                .user(user)
                .content(request.getContent())
                .imageLink(imageLink)
                .conversation(conversation)
                .build();
        return repo.save(message);
    }

}
