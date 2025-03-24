package io.github.hoangtuyen04work.social_backend.utils;

import io.github.hoangtuyen04work.social_backend.dto.response.PublicUserProfileResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserMapping {

    public Set<UserSummaryResponse> toUserSummaryResponses(Set<UserEntity> userEntities) {
        Set<UserSummaryResponse> userSummaryResponses = new HashSet<>();
        for (UserEntity userEntity : userEntities) {
            userSummaryResponses.add(toUserSummaryResponse(userEntity));
        }
        return userSummaryResponses;
    }

    public List<UserSummaryResponse> toUserSummaryResponses(List<UserEntity> userEntities) {
        List<UserSummaryResponse> userSummaryResponses = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            userSummaryResponses.add(toUserSummaryResponse(userEntity));
        }
        return userSummaryResponses;
    }

    public UserSummaryResponse toUserSummaryResponse(UserEntity userEntity) {
        return UserSummaryResponse.builder()
                .userId(userEntity.getId())
                .userName(userEntity.getUserName())
                .customId(userEntity.getCustomId())
                .imageLink(userEntity.getImageLink())
                .build();
    }

    public PublicUserProfileResponse toPublicProfile(UserEntity user){
        if (user == null) {
            return null;
        }
        PublicUserProfileResponse response = new PublicUserProfileResponse();
        response.setCustomId(user.getCustomId());
        response.setUserName(user.getUserName());
        response.setImageLink(user.getImageLink() != null ? user.getImageLink() : "");
        response.setBio(user.getBio() != null ? user.getBio() : "");
        response.setUserId(user.getId());
        return response;
    }

    public UserResponse toUserResponse(UserEntity user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setCustomId(user.getCustomId());
        response.setUserName(user.getUserName());
        response.setEmail(user.getEmail() != null ? user.getEmail() : ""); // Xử lý null thành chuỗi rỗng
        response.setPhone(user.getPhone() != null ? user.getPhone() : "");
        response.setImageLink(user.getImageLink() != null ? user.getImageLink() : "");
        response.setBio(user.getBio() != null ? user.getBio() : "");
        response.setDob(user.getDob() != null ? user.getDob() : null);
        response.setAddress(user.getAddress() != null ? user.getAddress() : "");
        return response;
    }

}
