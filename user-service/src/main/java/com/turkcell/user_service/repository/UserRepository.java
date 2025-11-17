package com.turkcell.user_service.repository;
import com.turkcell.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    User findByEmail(String email);
    Optional<User> findByUsername(String name);
}