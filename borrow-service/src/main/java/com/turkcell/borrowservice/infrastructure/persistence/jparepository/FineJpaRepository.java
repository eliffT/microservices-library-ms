package com.turkcell.borrowservice.infrastructure.persistence.jparepository;

import com.turkcell.borrowservice.infrastructure.persistence.entity.FineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FineJpaRepository extends JpaRepository<FineEntity, UUID> {

    // Üyenin açık cezası (isPaid=false) varsa yeni ödünç alamaz.
    boolean existsByUserIdAndIsPaid(UUID userId, boolean isPaid); // FineEntity'de userId olarak tanımladık

    // GET /api/fines/members/{memberId} sorgusunu desteklemek için.
    List<FineEntity> findByUserId(UUID userId);

    // Ödenmemiş/Ödenmiş cezaları sorgulamak için.
    List<FineEntity> findByUserIdAndIsPaid(UUID userId, boolean isPaid);
}
