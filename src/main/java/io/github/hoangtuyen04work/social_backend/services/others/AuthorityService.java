package io.github.hoangtuyen04work.social_backend.services.others;

import io.github.hoangtuyen04work.social_backend.entities.Authority;

public interface AuthorityService {
    Authority getAuthorityByName(String name);

    boolean existsAuthorityName(String name);

    boolean deleteAuthorityByName(String name);
}
