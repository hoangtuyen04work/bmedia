package io.github.hoangtuyen04work.social_backend.services.impl;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.entities.FriendshipEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.Friendship;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.FriendshipRepo;
import io.github.hoangtuyen04work.social_backend.services.FriendshipService;
import io.github.hoangtuyen04work.social_backend.services.UserService;
import io.github.hoangtuyen04work.social_backend.utils.UserMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    @Autowired
    private FriendshipRepo repo;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapping userMapping;

    @Override
    public Set<UserSummaryResponse> getMyFriend() throws AppException {
        List<UserSummaryResponse> myFriends = new ArrayList<>();
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<UserEntity> friend = repo.findByUser1Id(myId).get();
        friend.addAll(repo.findByUser1Id(myId).get());
        return userMapping.toUserSummaryResponses(friend);
    }

    //flag = 1 -> add ; flag = 2 -> accept; flag = 3 delete
    @Override
    public boolean changeFriendShip(String friendId, int flag) throws AppException {
        UserEntity user = userService.getUserCurrent();
        UserEntity friend = userService.findUserById(friendId);
        if(flag == 1){
            if(isFriend(user.getId(), friendId, 2) || !isFriend(user.getId(), friendId, 1))
                return false;
            FriendshipEntity friendship = FriendshipEntity.builder()
                    .user1(user)
                    .user2(friend)
                    .friendship(Friendship.PENDING)
                    .build();
            repo.save(friendship);
        }
        else if(flag == 2){
            if(!isFriend(user.getId(), friendId, 2)) return false;
            FriendshipEntity friendship = findByUserIdAndFriendId(user.getId(), friendId);
            friendship.setFriendship(Friendship.ACCEPTED);
            repo.save(friendship);
        }
        else{
            FriendshipEntity friendship = findByUserIdAndFriendId(user.getId(), friendId);
            repo.delete(friendship);
        }
        return true;
    }

    @Override
    public FriendshipEntity findByUserIdAndFriendId(String userId, String friendId) throws AppException {
        return repo.findByUserIdAndFriendId(userId, friendId)
                .orElse(repo.findByUserIdAndFriendId(friendId, userId)
                        .orElseThrow(() -> new AppException(ErrorCode.CONFLICT)));
    }

    @Override
    public boolean isFriend(String userId, String friendId, int flag){
        if(flag == 1 )
            if(repo.existsByUserIdAndFriendIdAndFriendship(userId, friendId, Friendship.ACCEPTED)) return true;
            else return  repo.existsByUserIdAndFriendIdAndFriendship(friendId, userId, Friendship.ACCEPTED);
        if(flag == 2 )
            if(repo.existsByUserIdAndFriendIdAndFriendship(userId, friendId, Friendship.PENDING)) return true;
            else return repo.existsByUserIdAndFriendIdAndFriendship(friendId, userId, Friendship.PENDING);
        return false;
    }
}
