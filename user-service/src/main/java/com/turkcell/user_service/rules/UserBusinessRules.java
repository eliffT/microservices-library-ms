package com.turkcell.user_service.rules;

import com.turkcell.user_service.entity.MembershipLevel;
import com.turkcell.user_service.entity.User;
import com.turkcell.user_service.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Component;

@Component
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
