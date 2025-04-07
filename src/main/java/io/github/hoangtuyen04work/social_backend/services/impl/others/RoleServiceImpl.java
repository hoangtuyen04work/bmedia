package io.github.hoangtuyen04work.social_backend.services.impl.others;

import io.github.hoangtuyen04work.social_backend.entities.RoleEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;
import io.github.hoangtuyen04work.social_backend.exception.ErrorCode;
import io.github.hoangtuyen04work.social_backend.repositories.RoleRepo;
import io.github.hoangtuyen04work.social_backend.services.others.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepo roleRepo;

    @Override
    public RoleEntity getRoleByRoleName(String name) throws AppException {
        return roleRepo.findByRoleName(name).orElseThrow(() -> new AppException((ErrorCode.CONFLICT)));
    }

}
