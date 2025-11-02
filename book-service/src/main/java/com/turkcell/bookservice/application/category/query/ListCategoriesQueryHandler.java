package com.turkcell.bookservice.application.category.query;

import com.turkcell.bookservice.application.category.dto.CategoryResponse;
import com.turkcell.bookservice.application.category.mapper.CategoryResponseMapper;
import com.turkcell.bookservice.core.cqrs.QueryHandler;
import com.turkcell.bookservice.domain.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListCategoriesQueryHandler implements QueryHandler<ListCategoriesQuery, List<CategoryResponse>> {

    private final CategoryRepository categoryRepository;
    private final CategoryResponseMapper categoryResponseMapper;

    public ListCategoriesQueryHandler(CategoryRepository categoryRepository, CategoryResponseMapper categoryResponseMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryResponseMapper = categoryResponseMapper;
    }

    @Override
    public List<CategoryResponse> handle(ListCategoriesQuery query) {
        return categoryRepository
                .findAllPaged(query.pageIndex(), query.pageSize())
                .stream()
                .map(categoryResponseMapper::toResponse)
                .toList();
    }
}
