package com.turkcell.user_service.application.service;

import com.turkcell.user_service.application.dto.UserRequest;
import com.turkcell.user_service.application.dto.UserResponse;
import com.turkcell.user_service.application.mapper.UserMapper;
import com.turkcell.user_service.infrastructure.persistence.entity.MembershipLevel;
import com.turkcell.user_service.infrastructure.persistence.entity.User;
import com.turkcell.user_service.infrastructure.persistence.repository.UserRepository;
import com.turkcell.user_service.application.rules.UserBusinessRules;
import jakarta.validation.Valid;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Validated
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserBusinessRules  userBusinessRules;


    public UserService(UserRepository userRepository, UserMapper userMapper,
                       UserBusinessRules userBusinessRules) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userBusinessRules = userBusinessRules;

    }

    public UserResponse createUser(@Valid UserRequest request) {
        // DTO - Entity
        User user = userMapper.toEntity(request);
        userRepository.save(user);
        // Entity - DTO
        return userMapper.toResponse(user);
    }

    public UserResponse updateUser(UUID id, @Valid UserRequest request) {

        User updateUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        updateUser.setFirstName(request.getFirstName());
        updateUser.setLastName(request.getLastName());
        updateUser.setEmail(request.getEmail());
        updateUser.setPhone(request.getPhone());
        userRepository.save(updateUser);

        return userMapper.toResponse(updateUser);
    }

    public void deleteUser(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        }
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        return userMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toResponseList(users);
    }

    public UserResponse findUserByEmail(String status, String email) {
        User user =  userRepository.findByEmail(email);
        String checkUserStatus = userBusinessRules.checkUserStatus(user);
        if(!checkUserStatus.equalsIgnoreCase(status)){
            throw new NotFoundException("User not found with email: " + email);
        }
        return userMapper.toResponse(user);
    }


    public UserResponse changeUserStatus(UUID id, String value) {
        User user =  userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found with id: " + id));
        if(!user.getMembershipLevel().equals(MembershipLevel.valueOf(value))){
            user.setMembershipLevel(MembershipLevel.valueOf(value));
            userRepository.save(user);
        }
        return userMapper.toResponse(user);
    }


}
