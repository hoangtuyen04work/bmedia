package io.github.hoangtuyen04work.social_backend.services.impl.users;

import io.github.hoangtuyen04work.social_backend.dto.request.UserCreationRequest;
import io.github.hoangtuyen04work.social_backend.entities.EmailCreationTemporaryEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.EmailCreationTemporaryRepo;
import io.github.hoangtuyen04work.social_backend.services.users.EmailCreationTemporaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailCreationTemporaryServiceImpl implements EmailCreationTemporaryService {

    @Autowired
    private EmailCreationTemporaryRepo repo;

    @Override
    public void save(UserCreationRequest request){
        repo.save(EmailCreationTemporaryEntity.builder()
                        .email(request.getEmail())
                        .userName(request.getUserName())
                        .password(request.getPassword())
                .build());
    }

    @Override
    public EmailCreationTemporaryEntity findByEmail(String email) throws AppException {
        return repo.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.NOT_AUTHENTICATION));
    }

    @Override
    public void delete(String email){
        repo.deleteByEmail(email);
    }
}
