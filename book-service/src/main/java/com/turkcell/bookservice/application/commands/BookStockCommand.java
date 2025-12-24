package com.turkcell.bookservice.application.commands;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

// Kitabın stoğunu düşürmek için kullanılan genel komut.
public record BookStockCommand(
        @NotNull UUID bookId,
        @NotNull UUID eventId
) {
}