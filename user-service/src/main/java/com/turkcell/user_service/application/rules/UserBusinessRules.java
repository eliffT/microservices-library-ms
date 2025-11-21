package com.turkcell.user_service.application.rules;

import com.turkcell.user_service.infrastructure.persistence.entity.MembershipLevel;
import com.turkcell.user_service.infrastructure.persistence.entity.User;
import com.turkcell.user_service.infrastructure.persistence.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;

public class UserBusinessRules {
    private final UserRepository userRepository;

    public UserBusinessRules(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Üye aktif mi cezalı mı?
    public String checkUserStatus(User user){
        if(user.membershipLevel() == MembershipLevel.BANNED){
            throw new NotFoundException("Member is banned");
        }
        return "ACTIVE";
    }
}
