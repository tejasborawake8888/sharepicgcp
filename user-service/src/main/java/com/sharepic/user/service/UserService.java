package com.sharepic.user.service;

import com.sharepic.user.entity.User;
import com.sharepic.user.enums.ErrorCode;
import com.sharepic.user.enums.Role;
import com.sharepic.user.exception.UserAlreadyExistsException;
import com.sharepic.user.exception.InvalidCredentialsException;
import com.sharepic.user.exception.UserNotFoundException;
import com.sharepic.user.model.*;
import com.sharepic.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ProfilePictureStorageService profilePicStorageService;

    public SignupResponse registerUser(SignupRequest request){
        boolean userExists = userRepository.existsByUserName(request.getUserName());
        boolean userEmailExists = userRepository.existsByEmail(request.getEmail());
        if(userExists){
            throw new UserAlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS.getMsg());
        }
        if(userEmailExists){
            throw new UserAlreadyExistsException(ErrorCode.USER_ALREADY_EXISTS.getMsg());
        }
        User newUser = new User(
                request.getUserName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER);
        newUser = userRepository.save(newUser);
        return new SignupResponse(
                newUser.getId(),
                newUser.getUserName(),
                newUser.getEmail(),
                newUser.getRole(),
                newUser.getCreatedAt());

    }

    public LoginResponse loginUser(LoginRequest request){
        User user = userRepository.findByUserNameOrEmail(request.getUserNameOrEmail(), request.getUserNameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException(ErrorCode.INVALID_CREDENTIALS.getMsg()));

        boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if(!isPasswordMatch){
            throw new InvalidCredentialsException(ErrorCode.INVALID_CREDENTIALS.getMsg());
        }
        String token = jwtService.generateToken(user);
        return new LoginResponse(user.getId(),user.getUserName(),user.getEmail(),user.getRole(),token);
    }

    public User getUserById(String id){
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND.getMsg()));
    }

    public UserProfileResponse updateUser(
            String id,
            String bio,
            MultipartFile profilePic){
        User user = getUserById(id);
        user.setBio(bio);

        if(profilePic != null && !profilePic.isEmpty()){
            String profilePicUrl = profilePicStorageService.uploadProfilePic(profilePic);
            user.setProfilePicUrl(profilePicUrl);
        }

        User updatedUser = userRepository.save(user);
        return new UserProfileResponse(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                updatedUser.getBio(),
                updatedUser.getProfilePicUrl()
        );
    }
}
