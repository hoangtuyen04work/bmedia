package io.github.hoangtuyen04work.social_backend.services.impl.users;
import io.github.hoangtuyen04work.social_backend.dto.response.FriendSummaryResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.entities.FriendshipEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.Friendship;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.FriendshipRepo;
import io.github.hoangtuyen04work.social_backend.services.conversations.ConversationService;
import io.github.hoangtuyen04work.social_backend.services.others.NotificationService;
import io.github.hoangtuyen04work.social_backend.services.users.FriendshipService;
import io.github.hoangtuyen04work.social_backend.services.users.UserService;
import io.github.hoangtuyen04work.social_backend.mapping.UserMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private NotificationService notificationService;

    @Override
    public Set<UserEntity> getMyFriend2()   {
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<UserEntity> friend = repo.findBySenderIdAndFriendship(myId, Friendship.ACCEPTED).get();
        friend.addAll(repo.findByReceiverIdAndFriendship(myId, Friendship.ACCEPTED).get());
        return friend;
    }

    @Override
    public PageResponse<UserSummaryResponse> getAllPending(Integer page, Integer size) {
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> friend = repo.findAllSendRequest(myId, pageable);
        return userMapping.toAllPendingResponse(friend);
    }

    @Override
    public PageResponse<UserSummaryResponse> getAllWaiting(Integer page, Integer size)  {
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> friend = repo.findAllAddFriendRequest(myId, pageable);
        return userMapping.toAllWaitingResponse(friend);
    }

    @Override
    public PageResponse<UserSummaryResponse> getAllAccepted(Integer page, Integer size){
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        Pageable pageable = PageRequest.of(page, size/2);
        Page<UserEntity> friend = repo.findAllSendRequest(myId, pageable);
        return  userMapping.toAllAcceptedResponse(friend);
    }

    //my friend all way accepte
    @Override
    public Set<FriendSummaryResponse> getMyFriend(){
        String myId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Object[]> res = repo.getAllFriendAndConversation(myId);
        Set<FriendSummaryResponse> result =  res.stream().map(obj ->
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
        return result;
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
            sendNotification(friend, user.getUserName() + " was send an add friend request.", "");
        }
        else if(flag == 2){
            if(!isFriend(friendId, user.getId(), 2))
                return false;
            FriendshipEntity friendship = findByUserIdAndFriendId(friendId, user.getId());
            friendship.setFriendship(Friendship.ACCEPTED);
            conversationService.createConversation(user, friend);
            repo.save(friendship);
            sendNotification(friend, user.getUserName() + " was accept your add friend request.", "");

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
    public void sendNotification(UserEntity receiver, String content, String title) throws AppException {
        notificationService.sendNotification(receiver, content, title);
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
