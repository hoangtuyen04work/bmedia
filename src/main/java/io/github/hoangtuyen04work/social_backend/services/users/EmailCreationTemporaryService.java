package io.github.hoangtuyen04work.social_backend.services.users;

import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.entities.EmailCreationTemporaryEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

public interface EmailCreationTemporaryService {
    void save(UserCreationRequest request);

    EmailCreationTemporaryEntity findByEmail(String email) throws AppException;

    void delete(String email);
}
