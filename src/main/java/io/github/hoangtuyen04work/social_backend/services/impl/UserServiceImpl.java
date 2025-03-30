package io.github.hoangtuyen04work.social_backend.services.impl;

import io.github.hoangtuyen04work.social_backend.dto.request.UserEditRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserLoginRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PublicUserProfileResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.UserSummaryResponse;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.Friendship;
import io.github.hoangtuyen04work.social_backend.enums.State;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.UserRepo;
import io.github.hoangtuyen04work.social_backend.services.RefreshTokenService;
import io.github.hoangtuyen04work.social_backend.services.RoleService;
import io.github.hoangtuyen04work.social_backend.services.UserService;
import io.github.hoangtuyen04work.social_backend.services.redis.TokenRedisService;
import io.github.hoangtuyen04work.social_backend.utils.Amazon3SUtils;
import io.github.hoangtuyen04work.social_backend.utils.UserMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapping userMapping;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private TokenRedisService tokenRedisService;
    @Autowired
    private Amazon3SUtils amazon3SUtils;


    @Override
    public PageResponse<UserSummaryResponse> searchByCustomId(String customId, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        String userId  = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<Object[]> res = userRepo.
                findFriendByCustomIdContainingAndState(".*" + customId +".*", userId, State.CREATED, pageable);
        List<UserSummaryResponse> ok = res.stream().map(obj -> new UserSummaryResponse(
                (String)obj[0],
                (String)obj[1],
                (String)obj[2],
                (String)obj[3],
                obj[4] != null ? Friendship.valueOf((String) obj[4]) : null
                )).toList();
        return PageResponse.<UserSummaryResponse>builder()
                .totalPages(res.getTotalPages())
                .totalElements(res.getTotalElements())
                .pageSize(res.getSize())
                .pageNumber(res.getNumber())
                .content(ok)
                .build();
    }

    @Override
    public UserResponse getCurrentUserInfo() throws AppException {
        UserEntity user = getUserCurrent();
        return userMapping.toUserResponse(user);
    }

    @Override
    public PublicUserProfileResponse getUserInfo(String customId) throws AppException {
        UserEntity user = findUserByCustomId(customId);
        return userMapping.toPublicProfile(user);
    }

    @Override
    public boolean deleteUser() throws AppException {
        UserEntity user = getUserCurrent();
        user.setCustomId("null");
        user.setEmail("null");
        user.setPhone("null");
        userRepo.save(user);
        tokenRedisService.deleteToken(user.getId());
        refreshTokenService.deleteRefreshTokenByUserId(user.getId());
        return true;
    }

    @Override
    public UserResponse changeInfo(UserEditRequest request) throws AppException {
        UserEntity user = getUserCurrent();
        user.setCustomId(request.getCustomId());
        user.setUserName(request.getUserName());
        user.setBio(request.getBio());
        user.setDob(request.getDob());
        user.setAddress(request.getAddress());
        String link = amazon3SUtils.addImageS3(request.getImageFile());
        if(link != null) user.setImageLink(link);
        userRepo.save(user);
        return userMapping.toUserResponse(user);
    }

    @Override
    public UserEntity changePassword(UserEntity user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepo.save(user);
    }

    @Override
    public boolean isRightPassword(String password) throws AppException {
        UserEntity user = getUserCurrent();
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public UserEntity getUserCurrent() throws AppException {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findById(userId).orElseThrow(()-> new AppException(ErrorCode.CONFLICT));
    }

    @Override
    public UserEntity findUserById(String id) throws AppException {
        return userRepo.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONFLICT));
    }

    @Override
    public UserEntity findUserByCustomId(String customId) throws AppException {
        return userRepo.findByCustomIdAndState(customId, State.CREATED).orElseThrow(() -> new AppException(ErrorCode.CONFLICT));
    }

    @Override
    public UserEntity loginByEmail(UserLoginRequest request) throws AppException {
        UserEntity user =  userRepo.findByEmailAndState(request.getEmail(), State.CREATED)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_AUTHENTICATION));
        if(passwordEncoder.matches(request.getPassword(), user.getPassword())) return user;
        throw new AppException(ErrorCode.NOT_AUTHENTICATION);
    }

    @Override
    public UserEntity loginByPhone(UserLoginRequest request) throws AppException {
        UserEntity user =  userRepo.findByPhoneAndState(request.getPhone(), State.CREATED)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_AUTHENTICATION));
        if(passwordEncoder.matches(request.getPassword(), user.getPassword())) return user;
        throw new AppException(ErrorCode.NOT_AUTHENTICATION);
    }

    @Override
    public UserEntity loginByCustomId(UserLoginRequest request) throws AppException {
        UserEntity user = userRepo.findByCustomIdAndState(request.getCustomId(), State.CREATED)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_AUTHENTICATION));
        if(passwordEncoder.matches(request.getPassword(), user.getPassword()))
            return user;
        throw new AppException(ErrorCode.NOT_AUTHENTICATION);
    }

    @Override
    public boolean existByEmail(String email){
        return userRepo.existsByEmailAndState(email, State.CREATED);
    }

    @Override
    public boolean existByPhone(String phone){
        return userRepo.existsByPhoneAndState(phone, State.CREATED);
    }

    @Override
    public boolean existByCustomId(String userId){
        return userRepo.existsByCustomIdAndState(userId, State.CREATED);
    }

    @Override
    public UserEntity createUserByCustomId(UserCreationRequest userCreationRequest) throws AppException {
        UserEntity user = UserEntity.builder()
                .customId(userCreationRequest.getCustomId())
                .userName(userCreationRequest.getUserName())
                .roles(Set.of(roleService.getRoleByRoleName("USER")))
                .password(passwordEncoder.encode(userCreationRequest.getPassword()))
                .build();
        return userRepo.save(user);
    }

    @Override
    public UserEntity createUserByEmail(UserCreationRequest userCreationRequest) throws AppException {
        UserEntity user = UserEntity.builder()
                .email(userCreationRequest.getEmail())
                .roles(Set.of(roleService.getRoleByRoleName("USER")))
                .customId(userCreationRequest.getCustomId())
                .userName(userCreationRequest.getUserName())
                .password(passwordEncoder.encode(userCreationRequest.getPassword()))
                .build();
        return userRepo.save(user);
    }

    @Override
    public boolean checkAttribute(UserCreationRequest request)  {
        return request.getCustomId() != null && !(request.getPassword() == null | request.getUserName() == null);
    }

    @Override
    public UserEntity createUserByPhone(UserCreationRequest userCreationRequest) throws AppException {
        UserEntity user = UserEntity.builder()
                .phone(userCreationRequest.getPhone())
                .customId(userCreationRequest.getCustomId())
                .userName(userCreationRequest.getUserName())
                .roles(Set.of(roleService.getRoleByRoleName("USER")))
                .password(passwordEncoder.encode(userCreationRequest.getPassword()))
                .build();
        return userRepo.save(user);
    }
}
