package com.turkcell.bookservice.application.category.query;

import com.turkcell.bookservice.application.category.dto.CategoryResponse;
import com.turkcell.bookservice.core.cqrs.Query;
import jakarta.validation.constraints.Min;

import java.util.List;

public record ListCategoriesQuery(
        @Min(0) Integer pageIndex,
        @Min(1) Integer pageSize)
        implements Query<List<CategoryResponse>> { }
