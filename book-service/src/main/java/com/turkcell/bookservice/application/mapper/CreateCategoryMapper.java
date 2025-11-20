package com.turkcell.bookservice.application.mapper;

import com.turkcell.bookservice.application.commands.CreateCategoryCommand;
import com.turkcell.bookservice.application.dto.CreatedCategoryResponse;
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

