package com.turkcell.borrowservice.domain.repository;

import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Fine;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FineRepository {
    Optional<Fine> findById(DomainId<Fine> id);
    Fine save(Fine fine);

    // Kural 2: Üyenin açık cezası (isPaid=false) varsa yeni ödünç alamaz.
    boolean existsByUserIdAndIsPaid(UUID userId, boolean isPaid);

    // GET /api/fines/members/{memberId}
    List<Fine> findByUserId(UUID userId);

    // Ödenmemiş cezaları sorgulamak için.
    List<Fine> findByUserIdAndIsPaid(UUID UserId, boolean isPaid);
}

