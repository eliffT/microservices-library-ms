package com.turkcell.bookservice.application.category.mapper;

import com.turkcell.bookservice.application.category.command.CreateCategoryCommand;
import com.turkcell.bookservice.application.category.dto.CreatedCategoryResponse;
import com.turkcell.bookservice.domain.model.category.Category;
import org.springframework.stereotype.Component;

@Component
public class CreateCategoryMapper {

    public Category toDomain(CreateCategoryCommand command) {
        return Category.create(
                command.name(),
                command.description()
        );
    }

    public CreatedCategoryResponse toResponse(Category category) {
        return new CreatedCategoryResponse(
                category.id().value(),
                category.name(),
                category.description()
        );
    }
}

