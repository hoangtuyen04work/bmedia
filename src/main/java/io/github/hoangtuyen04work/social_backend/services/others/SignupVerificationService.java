package io.github.hoangtuyen04work.social_backend.services.others;

import io.github.hoangtuyen04work.social_backend.entities.SignupVerificationEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

public interface SignupVerificationService {
    void saveSignupAuthentication(String id, String email);

    boolean verification(String email, String code) throws AppException;

    void delete(String email);

    SignupVerificationEntity getByCode(String code) throws AppException;
}
