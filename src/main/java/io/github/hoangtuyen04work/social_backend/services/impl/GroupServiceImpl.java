package io.github.hoangtuyen04work.social_backend.services.impl;
import io.github.hoangtuyen04work.social_backend.dto.request.GroupActionUser;
import io.github.hoangtuyen04work.social_backend.dto.request.GroupCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.GroupResponse;
import io.github.hoangtuyen04work.social_backend.entities.GroupEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.GroupRepo;
import io.github.hoangtuyen04work.social_backend.services.GroupService;
import io.github.hoangtuyen04work.social_backend.services.UserService;
import io.github.hoangtuyen04work.social_backend.utils.GroupMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepo repo;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupMapping groupMapping;

    @Override
    public GroupResponse createGroup(GroupCreationRequest request) throws AppException {
        UserEntity admin = userService.getUserCurrent();
        Set<UserEntity> users  = new HashSet<>();
        if(request.getUserId().isEmpty()){
            throw new AppException(ErrorCode.CONFLICT);
        }
        for(String userId : request.getUserId()){
            users.add(userService.findUserById(userId));
        }
        users.add(admin);
        GroupEntity group = GroupEntity.builder()
                .admin(admin)
                .name(request.getName())
                .users(users)
                .build();
        return groupMapping.toGroupResponse(group);
    }

    @Override
    public boolean deleteMember(GroupActionUser action) throws AppException {
        GroupEntity group = repo.findById(action.getGroupId())
                .orElseThrow(() -> new AppException(ErrorCode.CONFLICT));
        group.getUsers().size();
        Set<UserEntity> users = group.getUsers();
        users.removeIf(user -> action.getUserIds().contains(user.getId()));
        group.setUsers(users);
        repo.save(group);
        return true;
    }

    @Override
    public boolean addMember(GroupActionUser action) throws AppException {
        GroupEntity group = repo.findById(action.getGroupId())
                .orElseThrow(() -> new AppException(ErrorCode.CONFLICT));
        group.getUsers().size();
        Set<UserEntity> users = group.getUsers();
        Set<UserEntity> newUsers =  new HashSet<>();
        for(String userId : action.getUserIds()){
            UserEntity user = userService.findUserById(userId);
            newUsers.add(user);
        }
        users.addAll(newUsers);
        group.setUsers(users);
        repo.save(group);
        return true;
    }

    @Override
    public boolean deleteGroup(String groupId) throws AppException {
        repo.deleteById(groupId);
        return true;
    }
    @Override
    public GroupResponse changeName(GroupActionUser action) throws AppException {
        GroupEntity group = repo.findById(action.getGroupId())
                .orElseThrow(() -> new AppException(ErrorCode.CONFLICT));
        group.setName(action.getGroupName());
        return groupMapping.toGroupResponse(repo.save(group));
    }
}
