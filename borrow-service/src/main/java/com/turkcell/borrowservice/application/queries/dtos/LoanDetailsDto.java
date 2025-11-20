package com.turkcell.borrowservice.application.queries.dtos;

import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record LoanDetailsDto (
        UUID loanId,
        UUID bookId,
        OffsetDateTime borrowDate,
        OffsetDateTime dueDate,
        OffsetDateTime returnDate,
        LoanStatus status
) {
}
