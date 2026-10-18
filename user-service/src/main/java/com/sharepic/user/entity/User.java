package com.sharepic.user.entity;

import com.sharepic.user.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.jspecify.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true,length = 50)
    private String userName;

    @Column(nullable = false,unique = true,length = 200)
    private String email;

    @Column(name = "password_hash",nullable = false,length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private Role role;

    @Column(length = 500)
    private String bio;

    @Column(name = "profile_pic_url",length = 500)
    private String profilePicUrl;

    @Column(name = "created_at",nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false)
    private LocalDateTime updatedAt;

    public User(@NotBlank @Size(min = 3, max = 50) String userName, @Email @Size(max = 255) @NotBlank String email, @Nullable String encode, Role role) {
        this.userName = userName;
        this.email = email;
        this.passwordHash = encode;
        this.role = role;
    }

    @PrePersist
    protected void create(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName) && Objects.equals(email, user.email) && role == user.role && Objects.equals(bio, user.bio) && Objects.equals(profilePicUrl, user.profilePicUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, email, role, bio, profilePicUrl);
    }
}
