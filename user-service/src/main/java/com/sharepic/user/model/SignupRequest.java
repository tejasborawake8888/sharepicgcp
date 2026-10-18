package com.sharepic.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String userName;
    @NotBlank
    @Size(min = 8)
    private String password;
    @Email
    @Size(max = 255)
    @NotBlank
    private String email;
}
