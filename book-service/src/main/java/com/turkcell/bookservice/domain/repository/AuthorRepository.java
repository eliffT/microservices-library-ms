package com.turkcell.bookservice.domain.repository;

import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.author.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author save(Author author);
    Optional<Author> findById(DomainId<Author> authorId);
    List<Author> findAll();
    List<Author> findAllPaged(Integer pageIndex, Integer pageSize);
    void delete(DomainId<Author> authorId);
}
