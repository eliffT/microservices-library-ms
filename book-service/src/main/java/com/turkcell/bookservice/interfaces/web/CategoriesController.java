package com.turkcell.bookservice.interfaces.web;

import com.turkcell.bookservice.application.category.command.CreateCategoryCommand;
import com.turkcell.bookservice.application.category.dto.CategoryResponse;
import com.turkcell.bookservice.application.category.dto.CreatedCategoryResponse;
import com.turkcell.bookservice.application.category.query.ListCategoriesQuery;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.core.cqrs.QueryHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Validated
public class CategoriesController {
    private final QueryHandler<ListCategoriesQuery, List<CategoryResponse>> listCategoriesQueryHandler;
    private final CommandHandler<CreateCategoryCommand, CreatedCategoryResponse> createCategoryCommandHandler;

    public CategoriesController(
            QueryHandler<ListCategoriesQuery, List<CategoryResponse>> listCategoriesQueryHandler,
            CommandHandler<CreateCategoryCommand, CreatedCategoryResponse> createCategoryCommandHandler)
    {
        this.listCategoriesQueryHandler = listCategoriesQueryHandler;
        this.createCategoryCommandHandler = createCategoryCommandHandler;
    }

    @GetMapping
    public List<CategoryResponse> getCategories(@Valid ListCategoriesQuery query) {
        return listCategoriesQueryHandler.handle(query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedCategoryResponse createCategory(@Valid @RequestBody CreateCategoryCommand command) {
        return createCategoryCommandHandler.handle(command);
    }
}
