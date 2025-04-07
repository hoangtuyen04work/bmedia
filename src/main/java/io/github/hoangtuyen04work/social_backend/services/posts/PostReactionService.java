package io.github.hoangtuyen04work.social_backend.services.posts;

import io.github.hoangtuyen04work.social_backend.dto.request.ReactionPostRequest;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

public interface PostReactionService {
    Boolean reaction(ReactionPostRequest request, boolean isLike) throws AppException;
}
