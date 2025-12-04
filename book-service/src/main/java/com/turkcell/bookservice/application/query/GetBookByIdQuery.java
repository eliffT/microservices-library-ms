package com.turkcell.bookservice.application.query;

import com.turkcell.bookservice.application.dto.BookResponse;
import com.turkcell.bookservice.core.cqrs.Query;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetBookByIdQuery(
        @NotNull UUID bookId
) implements Query<BookResponse> { }