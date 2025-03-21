package io.github.hoangtuyen04work.social_backend.services;

import io.github.hoangtuyen04work.social_backend.dto.request.GroupActionUser;
import io.github.hoangtuyen04work.social_backend.dto.request.GroupCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.GroupResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

public interface GroupService {
    GroupResponse createGroup(GroupCreationRequest request) throws AppException;

    boolean addMember(GroupActionUser action) throws AppException;

    boolean deleteMember(GroupActionUser action) throws AppException;

    boolean deleteGroup(String groupId) throws AppException;

    GroupResponse changeName(GroupActionUser action) throws AppException;
}
