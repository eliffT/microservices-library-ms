package com.turkcell.bookservice.domain.repository;

import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.book.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(DomainId<Book> bookId);
    List<Book> findAll();
    List<Book> findAllPaged(Integer pageIndex, Integer pageSize);
    void delete(DomainId<Book> bookId);
}