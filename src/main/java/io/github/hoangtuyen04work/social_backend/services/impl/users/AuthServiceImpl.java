package io.github.hoangtuyen04work.social_backend.services.impl.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import io.github.hoangtuyen04work.social_backend.dto.request.ChangePasswordRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserLoginRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.AuthResponse;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.services.users.AuthService;
import io.github.hoangtuyen04work.social_backend.services.others.RefreshTokenService;
import io.github.hoangtuyen04work.social_backend.services.users.UserService;
import io.github.hoangtuyen04work.social_backend.utils.TokenUtils;
import io.github.hoangtuyen04work.social_backend.mapping.UserMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapping userMapping;
    
    @Autowired
    private RefreshTokenService refreshTokenService;
    
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserService userService;


    @Override
    public AuthResponse changePassword(ChangePasswordRequest request) throws JOSEException, AppException {
        UserEntity userEntity = userService.getUserCurrent();
        if( !  userService.isRightPassword(request.getOldPassword()))
            throw new AppException(ErrorCode.CONFLICT);
        userService.changePassword(userEntity, request.getNewPassword());
        refreshTokenService.deleteRefreshTokenByUserId(userEntity.getId());
        return AuthResponse.builder()
                .user(userMapping.toUserResponse(userEntity))
                .token(tokenUtils.generateToken(userEntity))
                .refreshToken(refreshTokenService.createRefreshTokenEntity(userEntity).getRefreshToken())
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) throws AppException, JOSEException {
        if(!refreshTokenService.isValidRefreshToken(refreshToken))
            throw new AppException(ErrorCode.REFRESH_TOKEN_INVALID);
        String userId = refreshTokenService.getUserIdByRefreshToken(refreshToken).getId();
        UserEntity userEntity = userService.findUserById(userId);
        refreshTokenService.deleteRefreshToken(refreshToken);
        return AuthResponse.builder()
                .user(userMapping.toUserResponse(userEntity))
                .token(tokenUtils.generateToken(userEntity))
                .refreshToken(refreshTokenService.createRefreshTokenEntity(userEntity).getRefreshToken())
                .build();
    }

    @Override
    public boolean logout(String token) throws ParseException {
        return tokenUtils.removeToken(token);
    }

    @Override
    public boolean authenticateToken(String token) throws ParseException, JOSEException  {
        return tokenUtils.checkToken(token);
    }

    @Override
    public AuthResponse signup(UserCreationRequest userCreationRequest) throws AppException, JOSEException{
        if(!userService.checkAttribute(userCreationRequest))
            throw  new AppException(ErrorCode.CONFLICT);
        if(userService.existByCustomId(userCreationRequest.getCustomId()))
            throw  new AppException(ErrorCode.CONFLICT);
        UserEntity userEntity = null;
        if(userCreationRequest.getEmail() != null && !userService.existByEmail(userCreationRequest.getEmail()))
            userEntity = userService.createUserByEmail(userCreationRequest);
        else if(userCreationRequest.getPhone() != null && !userService.existByPhone(userCreationRequest.getPhone()))
            userEntity = userService.createUserByPhone(userCreationRequest);
        else if(userCreationRequest.getCustomId() != null && !userService.existByCustomId(userCreationRequest.getCustomId()))
            userEntity = userService.createUserByCustomId(userCreationRequest);
        if(userEntity == null) throw new AppException(ErrorCode.CONFLICT);
        return AuthResponse.builder()
                .user(userMapping.toUserResponse(userEntity))
                .token(tokenUtils.generateToken(userEntity))
                .refreshToken(refreshTokenService.createRefreshTokenEntity(userEntity).getRefreshToken())
                .build();
    }

    @Override
    public AuthResponse login(UserLoginRequest request) throws AppException, JOSEException, JsonProcessingException {
        UserEntity userEntity;
        if(request.getEmail() != null && !request.getEmail().isEmpty()  && userService.existByEmail(request.getEmail()))
            userEntity = userService.loginByEmail(request);
        else if(request.getPhone() != null && !request.getPhone().isEmpty() && userService.existByPhone(request.getPhone()))
            userEntity = userService.loginByPhone(request);
        else
            userEntity = userService.loginByCustomId(request);
        refreshTokenService.deleteRefreshTokenByUserId(userEntity.getId());
        return AuthResponse.builder()
                .user(userMapping.toUserResponse(userEntity))
                .token(tokenUtils.generateToken(userEntity))
                .refreshToken(refreshTokenService.createRefreshTokenEntity(userEntity).getRefreshToken())
                .build();
    }
}
