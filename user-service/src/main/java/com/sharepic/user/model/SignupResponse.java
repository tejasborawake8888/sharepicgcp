package com.sharepic.user.model;

import com.sharepic.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponse {
    private UUID id;
    private String userName;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    public SignupResponse(String userName, String email, Role role, LocalDateTime createdAt) {
        this.userName = userName;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }
}
