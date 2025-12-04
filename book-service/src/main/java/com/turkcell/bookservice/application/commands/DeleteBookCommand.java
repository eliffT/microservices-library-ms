package com.turkcell.bookservice.application.commands;

import com.turkcell.bookservice.core.cqrs.Command;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeleteBookCommand(
        @NotNull UUID bookId)
        implements Command<Void> {
}