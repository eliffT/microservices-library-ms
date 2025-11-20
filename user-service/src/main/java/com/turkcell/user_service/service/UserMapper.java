package com.turkcell.user_service.service;


import com.turkcell.user_service.dto.UserRequest;
import com.turkcell.user_service.dto.UserResponse;
import com.turkcell.user_service.entity.User;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);
    User userRequestToUser(UserRequest userRequest);
    List<UserResponse> userToUserResponseList(List<User> users);

}