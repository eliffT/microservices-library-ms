package com.turkcell.borrowservice.application.mapper;

import com.turkcell.borrowservice.application.queries.dtos.LoanDetailsDto;
import com.turkcell.borrowservice.domain.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanAppMapper {

    public LoanDetailsDto toDto(Loan loan) {
        return new LoanDetailsDto(
                loan.id().value(),
                loan.bookId(),
                loan.borrowDate(),
                loan.dueDate(),
                loan.returnDate(),
                loan.status()
        );
    }
}
