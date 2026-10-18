package com.sharepic.user.controller;

import com.sharepic.user.model.LoginRequest;
import com.sharepic.user.model.LoginResponse;
import com.sharepic.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request){
        log.info("User Authentication started with User: {}", request.getUserNameOrEmail());
        return ResponseEntity.ok().body(userService.loginUser(request));
    }
}
