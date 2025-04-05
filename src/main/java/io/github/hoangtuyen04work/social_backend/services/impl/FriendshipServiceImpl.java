package io.github.hoangtuyen04work.social_backend.services.impl;
import io.github.hoangtuyen04work.social_backend.dto.response.FriendSummaryResponse;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
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


    //my friend all way accepte
    @Override
    public Set<FriendSummaryResponse> getMyFriend() {
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Object[]> res = repo.getAllFriendAndConversation(myId);
        Set<FriendSummaryResponse> ok = res.stream().map(obj ->
                new FriendSummaryResponse(
                        obj[0] != null ? (String)obj[0] : "",  // userId
                        obj[1] != null ? (String)obj[1] : "",  // customId
                        obj[2] != null ? (String)obj[2] : "",  // userName
                        obj[3] != null ? (String)obj[3] : "",  // imageLink
                        obj[4] != null ? (String)obj[4] : "",  // conversationId
                        obj[5] != null ? (String)obj[5] : "",  // newestMessage
                        obj[6] != null ? toInstant(obj[6]) : null,  // sendTime
                        obj[7] != null ? (String)obj[7] : ""   // senderId
                )).collect(Collectors.toSet());
        return ok;
    }
    private Instant toInstant(Object obj) {
        if (obj instanceof Instant) {
            return (Instant) obj;
        } else if (obj instanceof Timestamp) {
            return ((Timestamp) obj).toInstant();
        } else if (obj instanceof LocalDateTime) {
            return ((LocalDateTime) obj).toInstant(ZoneOffset.UTC);
        } else if (obj instanceof Date) {
            return ((Date) obj).toInstant();
        } else if (obj instanceof Long) {
            return Instant.ofEpochMilli((Long) obj);
        }
        throw new IllegalArgumentException("Unsupported type for sendTime: " + obj.getClass());
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
