package io.github.hoangtuyen04work.social_backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String content;
    String imageLink;
    String id;
    LocalDateTime creationDate;
    LocalDateTime modifiedDate;
    LocalDateTime deleteDate;
}
