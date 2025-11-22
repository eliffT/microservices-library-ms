package com.turkcell.bookservice.infrastructure.persistence.jparepository;

import com.turkcell.bookservice.infrastructure.persistence.entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorJpaRepository extends JpaRepository<AuthorEntity, UUID> {

}
