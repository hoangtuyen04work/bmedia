package io.github.hoangtuyen04work.social_backend.configuration;

import io.github.hoangtuyen04work.social_backend.entities.Authority;
import io.github.hoangtuyen04work.social_backend.entities.RoleEntity;
import io.github.hoangtuyen04work.social_backend.entities.UserEntity;
import io.github.hoangtuyen04work.social_backend.enums.State;
import io.github.hoangtuyen04work.social_backend.repositories.AuthorityRepo;
import io.github.hoangtuyen04work.social_backend.repositories.RoleRepo;
import io.github.hoangtuyen04work.social_backend.repositories.UserRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepo userRepo, RoleRepo roleRepo, AuthorityRepo authorityRepo){
        return args->{
            if(!authorityRepo.existsByName("SUPER_ADMIN")){
                Authority authority = Authority.builder()
                        .name("SUPER_ADMIN")
                        .build();
                authorityRepo.save(authority);
            }
            List<RoleEntity> roles = new ArrayList<>();
            if(!roleRepo.existsByRoleName("ADMIN")){
                RoleEntity admin = RoleEntity.builder()
                        .roleName("ADMIN")
                        .authorities(Set.of(authorityRepo.findByName("SUPER_ADMIN")))
                        .build();
                roleRepo.save(admin);
                roles.add(admin);
            }
            if(!roleRepo.existsByRoleName("USER")){
                RoleEntity user = RoleEntity.builder()
                        .roleName("USER")
                        .build();
                roleRepo.save(user);
                roles.add(user);
            }
            if(!userRepo.existsByCustomIdAndState("ADMIN", State.CREATED)){
                UserEntity user = UserEntity
                                .builder()
                                .customId("ADMIN")
                                .userName("ADMIN")
                                .password(passwordEncoder.encode("ADMIN"))
                                .build();
                userRepo.save(user);
            }
        };
    }
}