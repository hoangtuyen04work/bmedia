package io.github.hoangtuyen04work.social_backend.controller;

import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.request.GroupActionUser;
import io.github.hoangtuyen04work.social_backend.dto.request.GroupCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.GroupResponse;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.services.conversations.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PostMapping
    public ApiResponse<GroupResponse> createGroup(@RequestBody GroupCreationRequest request) throws AppException {
        return ApiResponse.<GroupResponse>builder()
                .data(groupService.createGroup(request))
                .build();
    }

    @PutMapping("/delete")
    public ApiResponse<Boolean> deleteMember(@RequestBody GroupActionUser request) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(groupService.deleteMember(request))
                .build();
    }

    @PutMapping("/add")
    public ApiResponse<Boolean> addMember(@RequestBody GroupActionUser request) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(groupService.addMember(request))
                .build();
    }

    @DeleteMapping
    public ApiResponse<Boolean> deleteGroup(@RequestBody String groupId) throws AppException {
        return ApiResponse.<Boolean>builder()
                .data(groupService.deleteGroup(groupId))
                .build();
    }

    @PutMapping("/changename")
    public ApiResponse<GroupResponse> changeName(@RequestBody GroupActionUser request) throws AppException {
        return ApiResponse.<GroupResponse>builder()
                .data(groupService.changeName(request))
                .build();
    }
}
