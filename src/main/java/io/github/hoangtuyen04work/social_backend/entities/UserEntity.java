package io.github.hoangtuyen04work.social_backend.entities;


import io.github.hoangtuyen04work.social_backend.entities.entityListener.UserListener;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.Set;
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(UserListener.class)
@Table(name = "users")
public class UserEntity extends FormEntity{
    @Column(nullable = false)
    String customId;
    @Column(nullable = false)
    String userName;
    @Column(nullable = false)
    String password;
    String imageLink;
    String email;
    String phone;
    String bio;
    Date dob;
    String address;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    RefreshTokenEntity refreshToken;

    @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    GroupEntity group;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    Set<FriendshipEntity> sendFriendRequests;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<FriendshipEntity> receiveFriendRequests;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL,  orphanRemoval = true)
    Set<PostEntity> posts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CommentEntity> comments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<MessageEntity> messages;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PostReactionEntity> postReactions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<MessageReactionEntity> messageReactions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<NotificationEntity> notifications;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name ="role_id")
    )
    Set<RoleEntity> roles;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<GroupEntity> groups;

    @ManyToMany(mappedBy = "taggedUsers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<CommentEntity> commentTags;
}


