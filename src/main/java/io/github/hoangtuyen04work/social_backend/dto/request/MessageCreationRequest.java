package io.github.hoangtuyen04work.social_backend.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageCreationRequest {
    String content;
    MultipartFile imageFile;
    String conversationId;
    String senderId;
    List<String> receiverId;
}
