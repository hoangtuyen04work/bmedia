package io.github.hoangtuyen04work.social_backend.services.impl;
import io.github.hoangtuyen04work.social_backend.entities.FriendshipEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.Friendship;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.repositories.FriendshipRepo;
import io.github.hoangtuyen04work.social_backend.services.FriendshipService;
import io.github.hoangtuyen04work.social_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {
    @Autowired
    private FriendshipRepo repo;
    @Autowired
    private UserService userService;

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
            FriendshipEntity friendship =
            repo.save(friendship);
        }
        else{
            FriendshipEntity friendship = FriendshipEntity.builder()
                    .user1(user)
                    .user2(friend)
                    .friendship(Friendship.ACCEPTED)
                    .build();
            repo.save(friendship);
        }
        return true;
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
