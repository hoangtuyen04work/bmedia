package io.github.hoangtuyen04work.social_backend.services.impl.conversations;
import io.github.hoangtuyen04work.social_backend.entities.ConversationEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.ConversationRepo;
import io.github.hoangtuyen04work.social_backend.services.conversations.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepo repo;



    @Override
    public boolean existConversation(String conservationId){
        return repo.existsById(conservationId);
    }

    @Override
    public ConversationEntity findById(String id) throws AppException {
        return repo.findById(id).orElseThrow(() -> new AppException(ErrorCode.CONFLICT));
    }

    @Override
    public ConversationEntity createConversation(UserEntity user1, UserEntity user2){
        ConversationEntity conversation = ConversationEntity.builder()
                .user1(user1)
                .user2(user2)
                .build();
        return repo.save(conversation);
    }
}
