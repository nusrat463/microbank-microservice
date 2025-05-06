package com.example.Authentication.controller;

import com.example.Authentication.dto.UserProfileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service") // Use service name from Eureka
public interface UserServiceClient {

    @PostMapping("/user/profile")
    void createUserProfile(@RequestBody UserProfileRequest request);
}

