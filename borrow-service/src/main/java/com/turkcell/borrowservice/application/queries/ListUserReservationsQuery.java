package com.turkcell.borrowservice.application.queries;

import com.turkcell.borrowservice.domain.model.enumstatus.ReservationStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Kullanıcı bazlı reservasyonları listeleme

public record ListUserReservationsQuery (
        @NotNull UUID userId,
        ReservationStatus statusFilter
) {}
