package io.github.hoangtuyen04work.social_backend.utils;

import io.github.hoangtuyen04work.social_backend.dto.response.UserResponse;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapping {

    public UserResponse toUserResponse(UserEntity user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(UUID.randomUUID().toString()); // Nếu cần sinh ID mới
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
