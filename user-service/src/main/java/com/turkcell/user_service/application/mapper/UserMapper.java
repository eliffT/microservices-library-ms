package com.turkcell.user_service.application.mapper;


import com.turkcell.user_service.application.dto.UserRequest;
import com.turkcell.user_service.application.dto.UserResponse;
import com.turkcell.user_service.infrastructure.persistence.entity.User;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);
    User userRequestToUser(UserRequest userRequest);
    List<UserResponse> userToUserResponseList(List<User> users);

}