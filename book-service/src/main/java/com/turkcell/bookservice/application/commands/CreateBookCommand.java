package com.turkcell.bookservice.application.commands;

import com.turkcell.bookservice.application.dto.CreatedBookResponse;
import com.turkcell.bookservice.core.cqrs.Command;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateBookCommand(
        @NotNull String authorId,
        @NotNull String publisherId,
        @NotNull String categoryId,
        @NotBlank @Size(min = 3, max = 255) String title,
        @NotNull @Min(1501) Integer year,
        @NotBlank @Size(min = 3, max = 14) String language,
        @NotNull Integer totalCopies,
        @NotNull @Positive BigDecimal price)
        implements Command<CreatedBookResponse> {
}

