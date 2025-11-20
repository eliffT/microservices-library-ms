package com.turkcell.borrowservice.infrastructure.persistence.jparepository;

import com.turkcell.borrowservice.domain.model.Loan;
import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;
import com.turkcell.borrowservice.infrastructure.persistence.entity.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanJpaRepository extends JpaRepository<LoanEntity, UUID> {
    // Aynı üyenin aynı kitabı aynı anda 2 kez ödünç alması yasak.
    Optional<LoanEntity> findByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, LoanStatus status);

    // GET /api/loans/members/{memberId}?status=OPEN
    List<LoanEntity> findByUserIdAndStatusIn(UUID userId, List<LoanStatus> statuses);

    // Scheduled job (Gecikmiş kontrolü) için
    List<LoanEntity> findByStatus(LoanStatus status);

    List<LoanEntity> findByUserId(UUID userId);
}
