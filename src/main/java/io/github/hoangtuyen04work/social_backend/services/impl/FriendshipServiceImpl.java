package io.github.hoangtuyen04work.social_backend.services.impl;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.entities.FriendshipEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.Friendship;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.FriendshipRepo;
import io.github.hoangtuyen04work.social_backend.services.ConversationService;
import io.github.hoangtuyen04work.social_backend.services.FriendshipService;
import io.github.hoangtuyen04work.social_backend.services.UserService;
import io.github.hoangtuyen04work.social_backend.utils.UserMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    @Autowired
    private FriendshipRepo repo;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapping userMapping;
    @Autowired
    private ConversationService conversationService;

    @Override
    public Set<UserEntity> getMyFriend2()   {
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<UserEntity> friend = repo.findBySenderIdAndFriendship(myId, Friendship.ACCEPTED).get();
        friend.addAll(repo.findByReceiverIdAndFriendship(myId, Friendship.ACCEPTED).get());
        return friend;
    }

    @Override
    public Set<UserSummaryResponse> getAllPending()   {
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<UserEntity> friend = repo.findByReceiverIdAndFriendship(myId, Friendship.PENDING).get();
        return userMapping.toUserSummaryResponses(friend);
    }


    //my friend all way accepted
    @Override
    public Set<UserSummaryResponse> getMyFriend()   {
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
//        Set<UserEntity> friend = repo.findBySenderIdAndFriendship(myId, Friendship.ACCEPTED).get();
//        friend.addAll(repo.findByReceiverIdAndFriendship(myId, Friendship.ACCEPTED).get());
        List<Object[]> res = repo.getAllFriendAndConversation(myId);
        Set<UserSummaryResponse> ok = res.stream().map(obj ->
                new UserSummaryResponse(
                        (String)obj[0],
                        (String)obj[1],
                        (String)obj[2],
                        (String)obj[3],
                        Friendship.ACCEPTED,
                        (String)obj[4]
                )).collect(Collectors.toSet());
        return ok;
    }


    //flag = 1 -> add ; flag = 2 -> accept; flag = 3 delete
    @Override
    public boolean changeFriendShip(String friendId, int flag) throws AppException {
        UserEntity user = userService.getUserCurrent();
        UserEntity friend = userService.findUserById(friendId);
        if(flag == 1){
            if(isFriend(user.getId(), friendId, 2)|| isFriend(user.getId(), friendId, 1))
                return false;
            FriendshipEntity friendship = FriendshipEntity.builder()
                    .sender(user)
                    .receiver(friend)
                    .friendship(Friendship.PENDING)
                    .build();
            repo.save(friendship);
        }
        else if(flag == 2){
            if(!isFriend(friendId, user.getId(), 2))
                return false;
            FriendshipEntity friendship = findByUserIdAndFriendId(friendId, user.getId());
            friendship.setFriendship(Friendship.ACCEPTED);
            conversationService.createConversation(user, friend);
            repo.save(friendship);
        }
        else{
            FriendshipEntity friendship = findByUserIdAndFriendId(user.getId(), friendId);
            repo.delete(friendship);
            friendship = findByUserIdAndFriendId(friendId, user.getId());
            repo.delete(friendship);
        }
        return true;
    }

    @Override
    public FriendshipEntity findByUserIdAndFriendId(String userId, String friendId) throws AppException {
        return repo.findByUserIdAndFriendId(userId, friendId)
                .orElseThrow(() -> new AppException(ErrorCode.CONFLICT));
    }

    @Override
    public boolean isFriend(String userId, String friendId, int flag){
        if(flag == 1 ) {
            boolean x = repo.existsBySenderIdAndReceiverIdAndFriendship(userId, friendId, Friendship.ACCEPTED);
            boolean y = repo.existsBySenderIdAndReceiverIdAndFriendship(friendId, userId, Friendship.ACCEPTED);
            if (x) return true;
            else return y;
        }
        if(flag == 2 ) {
            return repo.existsBySenderIdAndReceiverIdAndFriendship(userId, friendId, Friendship.PENDING);
        }
        return false;
    }
}
