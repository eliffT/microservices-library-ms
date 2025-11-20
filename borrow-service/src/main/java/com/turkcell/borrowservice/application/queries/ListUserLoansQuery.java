package com.turkcell.borrowservice.application.queries;

import com.turkcell.borrowservice.domain.model.enumstatus.LoanStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ListUserLoansQuery(
        @NotNull UUID userId,
        LoanStatus statusFilter
) {
}
