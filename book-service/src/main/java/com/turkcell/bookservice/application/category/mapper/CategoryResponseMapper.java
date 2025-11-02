package com.turkcell.bookservice.application.category.mapper;

import com.turkcell.bookservice.application.category.dto.CategoryResponse;
import com.turkcell.bookservice.domain.model.category.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryResponseMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.id().value(),
                category.name(),
                category.description()
        );
    }
}
