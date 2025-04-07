package io.github.hoangtuyen04work.social_backend.controller;


import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.request.MessageCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.MessageResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.conversations.MessageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    MessageService messageService;

    @MessageMapping("/conversation/{conversationId}")
    public void sendMessage(@Payload MessageCreationRequest request,
                            @DestinationVariable String conversationId) throws AppException, IOException {
        MessageResponse response = messageService.sendMessage(request);
        for(String receiverId : request.getReceiverId()){
            simpMessagingTemplate.convertAndSendToUser(receiverId, "/queue/conversation/messages/" + conversationId , response);
        }
    }


    @DeleteMapping("/conversation")
    public ApiResponse<Boolean> deleteMessage(@RequestBody String messageId){
        messageService.deleteById(messageId);
        return ApiResponse.<Boolean>builder()
                .data(true)
                .build();
    }

    @PutMapping("/conversation/{messageId}")
    public ApiResponse<MessageResponse> editMessage(@PathVariable String messageId,
                                                   @RequestBody String content) throws AppException {
        return ApiResponse.<MessageResponse>builder()
                .data(messageService.editMessage(messageId, content))
                .build();
    }

    @GetMapping("/conversation")
    public ApiResponse<List<MessageResponse>> getChat(@RequestParam String id,
                                                     @RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size){
        return ApiResponse.<List<MessageResponse>>builder()
                .data(messageService.getMessages(id, page, size))
                .build();
    }
}