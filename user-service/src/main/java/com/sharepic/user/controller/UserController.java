package com.sharepic.user.controller;

import com.sharepic.user.entity.User;
import com.sharepic.user.model.SignupRequest;
import com.sharepic.user.model.SignupResponse;
import com.sharepic.user.model.UserProfileResponse;
import com.sharepic.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

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

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getUser(Authentication authentication){
        log.info("Get user info");
        String userId = authentication.getPrincipal().toString();
        User user = userService.getUserById(userId);
        UserProfileResponse response = new UserProfileResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getBio(),
                user.getProfilePicUrl()
        );
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserProfileResponse> updateProfile(
            @RequestParam(required = false) String bio,
            @RequestParam(required = false)MultipartFile profilepic,
            Authentication authentication){
        log.info("profile update started");
        String userId = authentication.getPrincipal().toString();
        return ResponseEntity.ok().body(userService.updateUser(userId, bio, profilepic));
    }

    @PostMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SignupResponse> createAdmin(
            @Valid @RequestBody SignupRequest request) {
        log.info("creating admin user");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerAdmin(request));
    }
}
