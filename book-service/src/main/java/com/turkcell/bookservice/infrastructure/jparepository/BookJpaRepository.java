package com.turkcell.bookservice.infrastructure.jparepository;

import com.turkcell.bookservice.infrastructure.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookJpaRepository extends JpaRepository<BookEntity, UUID>{
}
