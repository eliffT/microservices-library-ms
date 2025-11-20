package com.turkcell.bookservice.application.commands.handler;

import com.turkcell.bookservice.application.commands.CreateCategoryCommand;
import com.turkcell.bookservice.application.dto.CreatedCategoryResponse;
import com.turkcell.bookservice.application.mapper.CreateCategoryMapper;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.domain.model.category.Category;
import com.turkcell.bookservice.domain.repository.CategoryRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateCategoryCommandHandler implements CommandHandler<CreateCategoryCommand, CreatedCategoryResponse> {

    private final CategoryRepository categoryRepository;
    private final CreateCategoryMapper mapper;

    public CreateCategoryCommandHandler(CategoryRepository categoryRepository, CreateCategoryMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CreatedCategoryResponse handle(CreateCategoryCommand command) {
        Category category = mapper.toDomain(command);
        category = categoryRepository.save(category);
        return mapper.toResponse(category);
    }
}