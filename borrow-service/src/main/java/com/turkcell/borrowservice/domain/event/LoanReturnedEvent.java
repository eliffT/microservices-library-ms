package com.turkcell.borrowservice.domain.event;

import java.time.LocalDate;
import java.util.UUID;

public record LoanReturnedEvent (UUID loanId,  UUID userId, UUID bookId, LocalDate returnDate){}




