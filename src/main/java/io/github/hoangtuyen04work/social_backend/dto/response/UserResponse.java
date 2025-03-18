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
public class UserResponse {
    String id;
    String userName;
    String customId;
    String email;
    String phone;
    String bio;
    Date dob;
    String address;
}
