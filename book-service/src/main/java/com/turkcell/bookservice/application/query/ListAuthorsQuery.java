package com.turkcell.bookservice.application.query;

import com.turkcell.bookservice.application.dto.AuthorResponse;
import com.turkcell.bookservice.core.cqrs.Query;
import jakarta.validation.constraints.Min;

import java.util.List;

public record ListAuthorsQuery(
        @Min(0) Integer pageIndex,
        @Min(1) Integer pageSize
) implements Query<List<AuthorResponse>> {}

