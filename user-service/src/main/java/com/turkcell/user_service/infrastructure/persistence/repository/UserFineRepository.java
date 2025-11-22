package com.turkcell.user_service.infrastructure.persistence.repository;

import com.turkcell.user_service.infrastructure.persistence.entity.UserFine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserFineRepository extends JpaRepository<UserFine, UUID> {
     // Kullanıcının toplam borcunu hesaplamak için metotlar eklenebilir.
     List<UserFine> findAllByUserIdAndIsPaidFalse(UUID userId);
}