package com.turkcell.user_service.interfaces.controller;

import com.turkcell.user_service.application.dto.UserRequest;
import com.turkcell.user_service.application.dto.UserResponse;
import com.turkcell.user_service.application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@Valid @RequestBody UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping("/update/{id}")
    public UserResponse updateUser(@PathVariable UUID id,
                                   @Valid @RequestBody UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/list-all")
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping
    public UserResponse findUserByEmail(@Valid @RequestParam String status,
                                        @Valid @RequestParam String email) {
        return userService.findUserByEmail(status, email);
    }

    @PutMapping("{id}/status")
    public UserResponse changeUserStatus(@PathVariable UUID id,
                                         @Valid @RequestParam String value) {
        return userService.changeUserStatus(id, value);
    }

    @GetMapping("/{id}/status")
    public String getUserStatus(@PathVariable UUID id) {
        return userService.getUserById(id).getMembershipLevel().toString();
    }
}
