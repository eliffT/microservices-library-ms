package com.turkcell.borrowservice.interfaces.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BorrowBookRequest(
        @NotNull(message = "ID cannot be null.")
        UUID userId,

        @NotNull(message = "Book ID cannot be null.")
        UUID bookId
) {}