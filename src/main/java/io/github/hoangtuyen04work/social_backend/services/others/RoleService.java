package io.github.hoangtuyen04work.social_backend.services.others;

import io.github.hoangtuyen04work.social_backend.entities.RoleEntity;
import io.github.hoangtuyen04work.social_backend.exception.AppException;

public interface RoleService {
    RoleEntity getRoleByRoleName(String name) throws AppException;
}
