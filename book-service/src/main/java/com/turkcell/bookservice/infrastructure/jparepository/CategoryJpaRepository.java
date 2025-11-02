package com.turkcell.bookservice.infrastructure.jparepository;

import com.turkcell.bookservice.infrastructure.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {
}
