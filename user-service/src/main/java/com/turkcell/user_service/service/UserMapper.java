package com.turkcell.user_service.service;


import com.turkcell.user_service.dto.UserRequest;
import com.turkcell.user_service.dto.UserResponse;
import com.turkcell.user_service.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class UserMapper {

    public UserResponse toResponse(User user){
        if (user == null)return null;
        UserResponse userResponse = new UserResponse(user.id(),
                user.firstName(),
                user.lastName(),
                user.email(),
                user.phone());
        return userResponse;
    }
    public User toEntity(UserRequest userRequest){
        if (userRequest == null)return  null;
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhone(userRequest.getPhone());
        return user;
    }
    public List<UserResponse> toResponseList(List<User> users){
        List<UserResponse> responseList = new ArrayList<>();
        for(User user:users ){
            UserResponse userResponse = new UserResponse(
                    user.id(),
                    user.firstName(),
                    user.lastName(),
                    user.email(),
                    user.phone()
            );
            responseList.add(userResponse);
        }
        return responseList;
    }

}