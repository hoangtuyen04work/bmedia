package io.github.hoangtuyen04work.social_backend.services.impl.others;

import io.github.hoangtuyen04work.social_backend.entities.SignupVerificationEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.SignupVerificationRepo;
import io.github.hoangtuyen04work.social_backend.services.others.SignupVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SignupVerificationServiceImpl implements SignupVerificationService {
    @Autowired
    private SignupVerificationRepo repo;

    @Override
    public void saveSignupAuthentication(String id, String email){
        repo.deleteByEmail(email);
        repo.save(SignupVerificationEntity.builder()
                        .id(id)
                        .email(email)
                        .expirationTime(LocalDateTime.now().plusMinutes(5))
                .build());
    }

    @Override
    public boolean verification(String email, String code) throws AppException {
        SignupVerificationEntity entity = repo.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return code.equals(entity.getId()) && entity.getExpirationTime().isAfter(LocalDateTime.now());
    }

    @Override
    public void delete(String email){
        repo.deleteByEmail(email);
    }

    @Override
    public SignupVerificationEntity getByCode(String code) throws AppException {
        return repo.findById(code).orElseThrow(() -> new AppException(ErrorCode.CONFLICT));
    }
}
