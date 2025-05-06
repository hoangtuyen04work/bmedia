package io.github.hoangtuyen04work.social_backend.services.impl.users;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import io.github.hoangtuyen04work.social_backend.services.others.RefreshTokenService;
import io.github.hoangtuyen04work.social_backend.services.others.RoleService;
import io.github.hoangtuyen04work.social_backend.services.redis.UserRedis;
import io.github.hoangtuyen04work.social_backend.services.users.UserService;
import io.github.hoangtuyen04work.social_backend.services.redis.TokenRedisService;
import io.github.hoangtuyen04work.social_backend.utils.Amazon3SUtils;
import io.github.hoangtuyen04work.social_backend.mapping.UserMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    @Autowired
    private UserRedis userRedis;


    @Override
    public PageResponse<UserSummaryResponse> searchByCustomId(String customId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        String userId  = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<Object[]> res = userRepo.
                findFriendByCustomIdContainingAndState(".*" + customId +".*", userId, State.CREATED, pageable);
        List<UserSummaryResponse> ok = res.stream().map(obj -> new UserSummaryResponse(
                (String)obj[0],
                (String)obj[1],
                (String)obj[2],
                (String)obj[3],
                obj[4] != null ? Friendship.valueOf((String) obj[4]) : null,
                (String)null
                )).toList();
        return  PageResponse.<UserSummaryResponse>builder()
                .totalPages(res.getTotalPages())
                .totalElements(res.getTotalElements())
                .pageSize(res.getSize())
                .pageNumber(res.getNumber())
                .content(ok)
                .build();
    }

    @Override
    public UserResponse getCurrentUserInfo() throws AppException, JsonProcessingException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse response = userRedis.getCurrentUserInfo(id);
        if(response != null)
            return response;
        UserEntity user = getUserCurrent();
        response = userMapping.toUserResponse(user);;
        userRedis.saveCurrentUserInfo(response);
        return  response;
    }


    @Override
    public PublicUserProfileResponse getUserInfo(String customId) throws AppException, JsonProcessingException {
        PublicUserProfileResponse response = userRedis.getUserInfo(customId);
        if(response != null) return response;
        UserEntity user = findUserByCustomId(customId);
        response =  userMapping.toPublicProfile(user);
        userRedis.saveUserInfo(response);
        return response;
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
    public UserResponse changeInfo(UserEditRequest request) throws AppException, ParseException {
        UserEntity user = getUserCurrent();
        if(request.getCustomId() != null)
            user.setCustomId(request.getCustomId());
        if(request.getUserName() != null)
            user.setUserName(request.getUserName());
        if(request.getBio() != null)
            user.setBio(request.getBio());
        if(request.getEmail() != null)
            user.setEmail(request.getEmail());
        if(request.getPhone() != null)
            user.setPhone(request.getPhone());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dob = request.getDob() != null ? dateFormat.parse(request.getDob()) : null;
        user.setDob(dob);
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
    public UserEntity findUserByCustomId(String customId) throws AppException, JsonProcessingException {
        UserEntity user = userRedis.getFindUserByCustomId(customId);
        if(user != null) return user;
        user =  userRepo.findByCustomIdAndState(customId, State.CREATED)
                .orElseThrow(() -> new AppException(ErrorCode.CONFLICT));
        userRedis.saveFindUserByCustomId(user, customId);
        return user;
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
