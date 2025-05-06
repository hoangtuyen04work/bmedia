package io.github.hoangtuyen04work.social_backend.dto.response;

import io.github.hoangtuyen04work.social_backend.enums.Friendship;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSummaryResponse {
    String userId;
    String customId;
    String userName;
    String imageLink;
    Friendship friendship;
    String conversationId;
}
