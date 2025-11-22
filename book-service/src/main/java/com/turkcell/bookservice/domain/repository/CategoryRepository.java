package com.turkcell.bookservice.domain.repository;

import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.category.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);
    Optional<Category> findById(DomainId<Category> categoryId);
    List<Category> findAll();
    List<Category> findAllPaged(Integer pageIndex, Integer pageSize);
    void delete(DomainId<Category> categoryId);
}
