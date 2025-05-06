package io.github.hoangtuyen04work.social_backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    String content;
    String imageLink;
    String id;
    LocalDate creationDate;
    LocalDate modifiedDate;
    LocalDate deleteDate;
    String customId;
    String userName;
    String userId;
    String avatarLink;
}
