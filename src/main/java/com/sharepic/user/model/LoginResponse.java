package com.sharepic.user.model;

import com.sharepic.user.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private UUID id;
    private String userName;
    private String email;
    private Role role;
    private String accessToken;
}
