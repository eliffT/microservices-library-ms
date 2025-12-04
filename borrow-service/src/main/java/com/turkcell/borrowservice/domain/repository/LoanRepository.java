package com.turkcell.borrowservice.domain.repository;

import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Loan;
import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoanRepository {
    Optional<Loan> findById(DomainId<Loan> id);
    Loan save(Loan loan);
    // Kural 3: Aynı üyenin aynı kitabı aynı anda 2 kez ödünç alması yasak.
    Optional<Loan> findByUserIdAndBookIdAndStatus(UUID userId, UUID bookId, LoanStatus status);
    // GET /api/loans/members/{memberId}?status=OPEN
    List<Loan> findByUserIdAndStatusIn(UUID userId, List<LoanStatus> statuses);
    long countByUserIdAndStatusIn(UUID userId, List<LoanStatus> statuses);
    // Scheduled job (Gecikmiş kontrolü)
    List<Loan> findByStatus(LoanStatus status);
    void deleteById(DomainId<Loan> id);
    List<Loan> findByUserId(UUID userId);
}
