package io.github.hoangtuyen04work.social_backend.services.impl.users;

import com.nimbusds.jose.JOSEException;
import io.github.hoangtuyen04work.social_backend.dto.request.ChangePasswordRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.dto.request.UserLoginRequest;
import io.github.hoangtuyen04work.social_backend.dto.response.AuthResponse;
import io.github.hoangtuyen04work.social_backend.entities.EmailCreationTemporaryEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.services.others.EmailService;
import io.github.hoangtuyen04work.social_backend.services.others.SignupVerificationService;
import io.github.hoangtuyen04work.social_backend.services.users.AuthService;
import io.github.hoangtuyen04work.social_backend.services.others.RefreshTokenService;
import io.github.hoangtuyen04work.social_backend.services.users.EmailCreationTemporaryService;
import io.github.hoangtuyen04work.social_backend.services.users.UserService;
import io.github.hoangtuyen04work.social_backend.utils.TokenUtils;
import io.github.hoangtuyen04work.social_backend.mapping.UserMapping;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.UUID;

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

    @Autowired
    private SignupVerificationService signupVerificationService;

    @Autowired
    private EmailCreationTemporaryService emailCreationTemporaryService;

    @Autowired
    private EmailService emailService;

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
    public String generateSignupVerificationCode(){
        return UUID.randomUUID().toString();
    }


    @Override
    public boolean  signupByEmail(UserCreationRequest userCreationRequest)
            throws MessagingException, AppException {
        if(userCreationRequest.getEmail() != null && !userService.existByEmail(userCreationRequest.getEmail()))
        {
            String code = generateSignupVerificationCode();
            signupVerificationService.saveSignupAuthentication(code, userCreationRequest.getEmail());
            emailService.signupVerify(userCreationRequest.getEmail(), code);
            emailCreationTemporaryService.delete(userCreationRequest.getEmail());
            emailCreationTemporaryService.save(userCreationRequest);
            return true;
        }
        throw new AppException(ErrorCode.NOT_AUTHENTICATION);
    }

    @Override
    public AuthResponse verifySignupByEmail(String email, String code, String customId)
            throws AppException, JOSEException {
        if(verifyEmailCode(email, code) && !userService.existByCustomId(customId)){
            EmailCreationTemporaryEntity entity = emailCreationTemporaryService.findByEmail(email);
            UserCreationRequest userCreationRequest = userMapping.toUserCreation(entity);
            userCreationRequest.setCustomId(customId);
            UserEntity userEntity = userService.createUserByEmail(userCreationRequest);
            return AuthResponse.builder()
                    .user(userMapping.toUserResponse(userEntity))
                    .token(tokenUtils.generateToken(userEntity))
                    .refreshToken(refreshTokenService.createRefreshTokenEntity(userEntity).getRefreshToken())
                    .build();
        }
        throw new AppException(ErrorCode.NOT_AUTHENTICATION);
    }

    @Override
    public boolean verifyEmailCode(String email, String code) throws AppException {
        if(signupVerificationService.verification(email, code)){
            signupVerificationService.delete(email);
            return true;
        }
        return false;
    }

    @Override
    public AuthResponse signup(UserCreationRequest userCreationRequest)
            throws AppException, JOSEException {
        if(!userService.checkAttribute(userCreationRequest))
            throw  new AppException(ErrorCode.CONFLICT);
        if(userService.existByCustomId(userCreationRequest.getCustomId()))
            throw  new AppException(ErrorCode.CONFLICT);
        UserEntity userEntity = null;
        if(userCreationRequest.getPhone() != null && !userService.existByPhone(userCreationRequest.getPhone()))
            userEntity = userService.createUserByPhone(userCreationRequest);
        else if(userCreationRequest.getCustomId() != null
                && !userService.existByCustomId(userCreationRequest.getCustomId()))
            userEntity = userService.createUserByCustomId(userCreationRequest);
        if(userEntity == null) throw new AppException(ErrorCode.CONFLICT);
        return AuthResponse.builder()
                .user(userMapping.toUserResponse(userEntity))
                .token(tokenUtils.generateToken(userEntity))
                .refreshToken(refreshTokenService.createRefreshTokenEntity(userEntity).getRefreshToken())
                .build();
    }

    @Override
    public AuthResponse login(UserLoginRequest request) throws AppException, JOSEException {
        UserEntity userEntity;
        if(request.getEmail() != null && !request.getEmail().isEmpty()
                && userService.existByEmail(request.getEmail()))
            userEntity = userService.loginByEmail(request);
        else if(request.getPhone() != null && !request.getPhone().isEmpty()
                && userService.existByPhone(request.getPhone()))
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
