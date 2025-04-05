package io.github.hoangtuyen04work.social_backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendSummaryResponse {
    String userId;
    String customId;
    String userName;
    String imageLink;
    String conversationId;
    String newestMessage;
    Instant sendTime;
    String senderId;
}
