package io.github.hoangtuyen04work.social_backend.controller;

import io.github.hoangtuyen04work.social_backend.dto.ApiResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.NotificationResponse;
import io.github.hoangtuyen04work.social_backend.dto.response.PageResponse;
import io.github.hoangtuyen04work.social_backend.services.others.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    @Autowired
    private NotificationService service;

    @GetMapping()
    public ApiResponse<PageResponse<NotificationResponse>> getNotification
            (@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size){
        return ApiResponse.<PageResponse<NotificationResponse>>builder()
                .data(service.getAllNotification(page, size))
                .build();
    }

    @PostMapping("/read")
    public ApiResponse<Boolean> readNotification(){
        return ApiResponse.<Boolean>builder()
                .data(service.readNotification())
                .build();
    }
}
