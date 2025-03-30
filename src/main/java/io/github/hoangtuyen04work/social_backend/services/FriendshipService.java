package io.github.hoangtuyen04work.social_backend.services;

import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.entities.FriendshipEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

import java.util.Set;

public interface FriendshipService {
    Set<UserEntity> getMyFriend2();

    Set<UserSummaryResponse> getAllPending();

    Set<UserSummaryResponse> getMyFriend() throws AppException;

    //flag = 1 -> add ; flag = 2 -> accept; flag = 3 delete
    boolean changeFriendShip(String friendId, int flag) throws AppException;

    FriendshipEntity findByUserIdAndFriendId(String userId, String friendId) throws AppException;

    boolean isFriend(String userId, String friendId, int flag);
}
