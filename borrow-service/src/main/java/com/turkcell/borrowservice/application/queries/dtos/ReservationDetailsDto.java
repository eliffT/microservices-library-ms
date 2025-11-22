package com.turkcell.borrowservice.application.queries.dtos;

import com.turkcell.borrowservice.domain.model.enumstatus.ReservationStatus;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ReservationDetailsDto (
        UUID reservationId,
        UUID bookId,
        OffsetDateTime reservationDate,
        OffsetDateTime expireDate,
        ReservationStatus status
) {}
