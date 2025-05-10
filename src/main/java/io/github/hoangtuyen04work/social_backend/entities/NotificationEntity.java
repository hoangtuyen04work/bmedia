package io.github.hoangtuyen04work.social_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "notifications")
public class NotificationEntity  extends FormEntity{
    String title;
    String content;
    Boolean isRead;
    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;
}
