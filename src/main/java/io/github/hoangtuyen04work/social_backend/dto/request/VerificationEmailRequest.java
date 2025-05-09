package io.github.hoangtuyen04work.social_backend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationEmailRequest {
    String email;
    String code;
    String customId;
}
