package com.turkcell.bookservice.infrastructure.persistence.adapter;

import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.category.Category;
import com.turkcell.bookservice.domain.repository.CategoryRepository;
import com.turkcell.bookservice.infrastructure.persistence.entity.CategoryEntity;
import com.turkcell.bookservice.infrastructure.persistence.jparepository.CategoryJpaRepository;
import com.turkcell.bookservice.infrastructure.mapper.CategoryEntityMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryRepositoryAdapter implements CategoryRepository {
    private final CategoryJpaRepository repository;
    private final CategoryEntityMapper mapper;

    public CategoryRepositoryAdapter(CategoryJpaRepository repository, CategoryEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Category save(Category category) {
        CategoryEntity categoryEntity = mapper.toEntity(category);
        categoryEntity = repository.save(categoryEntity);
        return mapper.toDomain(categoryEntity);
    }

    @Override
    public Optional<Category> findById(DomainId<Category> categoryId) {
        return repository.findById(categoryId.value()).map(mapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Category> findAllPaged(Integer pageIndex, Integer pageSize) {
        return repository.findAll(PageRequest.of(pageIndex,pageSize))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(DomainId<Category> categoryId) {
        repository.deleteById(categoryId.value());
    }
}

