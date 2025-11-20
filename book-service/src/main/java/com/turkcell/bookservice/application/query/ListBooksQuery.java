package com.turkcell.bookservice.application.query;

import com.turkcell.bookservice.application.dto.BookResponse;
import com.turkcell.bookservice.core.cqrs.Query;
import jakarta.validation.constraints.Min;

import java.util.List;

public record ListBooksQuery (
        @Min(0) Integer pageIndex,
        @Min(1) Integer pageSize
) implements Query<List<BookResponse>> {

}
