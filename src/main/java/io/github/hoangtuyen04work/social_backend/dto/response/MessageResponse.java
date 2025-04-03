package io.github.hoangtuyen04work.social_backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    String content;
    String imageLink;
    String senderId;
    Instant sendAt;
}
