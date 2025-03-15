package io.github.hoangtuyen04work.social_backend.entities;

import io.github.hoangtuyen04work.social_backend.enums.Reaction;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostEntity extends FormEntity{
    String content;
    String imageLink;
    @Enumerated(EnumType.STRING)
    Reaction reaction;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity user;

    @OneToMany(mappedBy = "post")
    Set<CommentEntity> comments;

    @OneToMany(mappedBy = "post")
    Set<PostReactionEntity> postReactions;
}
