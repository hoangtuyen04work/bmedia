package io.github.hoangtuyen04work.social_backend.services.impl.posts;

import io.github.hoangtuyen04work.social_backend.dto.request.ReactionPostRequest;
import io.github.hoangtuyen04work.social_backend.entities.PostEntity;
import io.github.hoangtuyen04work.social_backend.entities.PostReactionEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.Reaction;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.repositories.PostReactionRepo;
import io.github.hoangtuyen04work.social_backend.services.posts.PostReactionService;
import io.github.hoangtuyen04work.social_backend.services.posts.PostService;
import io.github.hoangtuyen04work.social_backend.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReactionServiceImpl implements PostReactionService {
    @Autowired
    private PostReactionRepo repo;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @Override
    public Boolean reaction(ReactionPostRequest request, boolean isLike) throws AppException {
        if(isLike){
            UserEntity user = userService.getUserCurrent();
            PostEntity post = postService.findById(request.getPostId());
            PostReactionEntity entity = PostReactionEntity.builder()
                    .reaction(Reaction.LIKE)
                    .post(post)
                    .user(user)
                    .build();
            repo.save(entity);
        }
        else{
            repo.deleteByUserIdAndPostId(request.getUserId(), request.getPostId());
        }
        return true;
    }
}
