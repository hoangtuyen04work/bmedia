package io.github.hoangtuyen04work.social_backend.services.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.hoangtuyen04work.social_backend.dto.response.FriendSummaryResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.entities.FriendshipEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

import java.util.Set;

public interface FriendshipService {
    Set<UserEntity> getMyFriend2();

    PageResponse<UserSummaryResponse> getAllPending(Integer page, Integer size) throws JsonProcessingException;

    PageResponse<UserSummaryResponse> getAllWaiting(Integer page, Integer size) throws JsonProcessingException;

    PageResponse<UserSummaryResponse> getAllAccepted(Integer page, Integer size) throws JsonProcessingException;


    Set<FriendSummaryResponse> getMyFriend() throws AppException, JsonProcessingException;

    //flag = 1 -> add ; flag = 2 -> accept; flag = 3 delete
    boolean changeFriendShip(String friendId, int flag) throws AppException;

    FriendshipEntity findByUserIdAndFriendId(String userId, String friendId) throws AppException;

    boolean isFriend(String userId, String friendId, int flag);
}
