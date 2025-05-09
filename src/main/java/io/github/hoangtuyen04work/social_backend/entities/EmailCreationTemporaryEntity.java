package io.github.hoangtuyen04work.social_backend.entities;

import io.github.hoangtuyen04work.social_backend.entities.entityListener.CommentListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class EmailCreationTemporaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String customId;
    String userName;
    String password;
    String email;
    String phone;
    String bio;
    Date dob;
    String address;
}
