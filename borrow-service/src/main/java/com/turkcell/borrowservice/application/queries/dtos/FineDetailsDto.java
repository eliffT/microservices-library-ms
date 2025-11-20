package com.turkcell.borrowservice.application.queries.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record FineDetailsDto (
        UUID fineId,
        UUID loanId,
        BigDecimal amount,
        OffsetDateTime paymentDate,
        boolean isPaid
) {
}
