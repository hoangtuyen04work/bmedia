package io.github.hoangtuyen04work.social_backend.entities;

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
@Table(name = "groups")
public class GroupEntity extends FormEntity {
    @Column(nullable = false)
    String name;
    String imageLink;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    Set<MessageEntity> messages;

    @ManyToMany
    @JoinTable(
            name = "group_user",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    Set<UserEntity> users;
}
