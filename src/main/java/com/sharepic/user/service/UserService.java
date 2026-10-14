package com.sharepic.user.service;

import com.sharepic.user.entity.User;
import com.sharepic.user.enums.ErrorCode;
import com.sharepic.user.enums.Role;
import com.sharepic.user.exception.UserAlreadyExistsException;
import com.sharepic.user.model.SignupRequest;
import com.sharepic.user.model.SignupResponse;
import com.sharepic.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
