package com.turkcell.borrowservice.application.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReturnBookCommand (
    @NotNull UUID loanId) {
}
