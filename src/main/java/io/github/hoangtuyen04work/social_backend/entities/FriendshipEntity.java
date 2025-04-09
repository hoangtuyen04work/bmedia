package io.github.hoangtuyen04work.social_backend.entities;

import io.github.hoangtuyen04work.social_backend.entities.entityListener.FriendListener;
import io.github.hoangtuyen04work.social_backend.enums.Friendship;
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
@Table(name = "friendship")
@EntityListeners(FriendListener.class)
public class FriendshipEntity extends FormEntity{
    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    UserEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver", nullable = false)
    UserEntity receiver;

    @Enumerated(EnumType.STRING)
    Friendship friendship;
}
