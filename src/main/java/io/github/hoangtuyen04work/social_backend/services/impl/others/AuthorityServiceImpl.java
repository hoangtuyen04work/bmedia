package io.github.hoangtuyen04work.social_backend.services.impl.others;

import io.github.hoangtuyen04work.social_backend.entities.Authority;
import io.github.hoangtuyen04work.social_backend.repositories.AuthorityRepo;
import io.github.hoangtuyen04work.social_backend.services.others.AuthorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    private AuthorityRepo repo;

    @Override
    public Authority getAuthorityByName(String name){
        return repo.findByName(name);
    }

    @Override
    public boolean existsAuthorityName(String name){
        return repo.existsByName(name);
    }

    @Override
    public boolean deleteAuthorityByName(String name){
        return repo.deleteByName(name);
    }
}
