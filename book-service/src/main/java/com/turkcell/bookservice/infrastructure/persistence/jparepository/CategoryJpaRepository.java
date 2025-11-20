package com.turkcell.bookservice.infrastructure.persistence.jparepository;

import com.turkcell.bookservice.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {
}
