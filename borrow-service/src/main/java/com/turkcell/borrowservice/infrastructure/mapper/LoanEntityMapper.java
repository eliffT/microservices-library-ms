package com.turkcell.borrowservice.infrastructure.mapper;


import com.turkcell.borrowservice.domain.model.DomainId;
import com.turkcell.borrowservice.domain.model.Loan;
import com.turkcell.borrowservice.infrastructure.persistence.entity.LoanEntity;
import org.springframework.stereotype.Component;


@Component
public class LoanEntityMapper {
    public LoanEntity toEntity(Loan loan) {
        if (loan == null) return null;

        LoanEntity loanEntity = new LoanEntity();
        loanEntity.setId(loan.id().value());
        loanEntity.setUserId(loan.userId());
        loanEntity.setBookId(loan.bookId());
        loanEntity.setBorrowDate(loan.borrowDate());
        loanEntity.setDueDate(loan.dueDate());
        loanEntity.setReturnDate(loan.returnDate());
        loanEntity.setStatus(loan.status());

        return loanEntity;
    }

    public Loan toDomain(LoanEntity entity) {
        if (entity == null) return null;

        return Loan.rehydrate(
                new DomainId<>(entity.getId()),
                entity.getUserId(),
                entity.getBookId(),
                entity.getBorrowDate(),
                entity.getDueDate(),
                entity.getReturnDate(),
                entity.getStatus());
    }
}
