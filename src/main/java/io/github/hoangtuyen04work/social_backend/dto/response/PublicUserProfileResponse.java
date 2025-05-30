package io.github.hoangtuyen04work.social_backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicUserProfileResponse {
    String userId;
    String userName;
    String imageLink;
    String customId;
    String bio;
}
