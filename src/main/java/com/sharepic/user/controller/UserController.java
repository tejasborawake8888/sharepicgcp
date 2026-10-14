package com.sharepic.user.controller;

import com.sharepic.user.model.SignupRequest;
import com.sharepic.user.model.SignupResponse;
import com.sharepic.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping
    public ResponseEntity<SignupResponse> registerUser(@Valid @RequestBody SignupRequest request){
        log.info("User registration called");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(request));

    }
}
