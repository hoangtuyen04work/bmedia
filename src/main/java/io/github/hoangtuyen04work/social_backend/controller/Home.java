package io.github.hoangtuyen04work.social_backend.controller;

import io.github.hoangtuyen04work.social_backend.enums.State;
import io.github.hoangtuyen04work.social_backend.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Home {

    @Autowired
    private UserRepo repo;

}

