package com.turkcell.borrowservice.application.commands;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record BorrowBookCommand(
       @NotNull UUID userId,
       @NotNull UUID bookId,
       @Positive int loanDays
) {
}