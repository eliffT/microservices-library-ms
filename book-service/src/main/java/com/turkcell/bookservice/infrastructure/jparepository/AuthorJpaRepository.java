package com.turkcell.bookservice.infrastructure.jparepository;

import com.turkcell.bookservice.infrastructure.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorJpaRepository extends JpaRepository<AuthorEntity, UUID> {

}
