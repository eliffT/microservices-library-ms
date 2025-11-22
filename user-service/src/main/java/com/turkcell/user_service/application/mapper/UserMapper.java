package com.turkcell.user_service.application.mapper;


import com.turkcell.user_service.application.dto.UserRequest;
import com.turkcell.user_service.application.dto.UserResponse;
import com.turkcell.user_service.infrastructure.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    UserResponse userToUserResponse(User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "membershipLevel", ignore = true)
    User userRequestToUser(UserRequest userRequest);
    List<UserResponse> userToUserResponseList(List<User> users);

}