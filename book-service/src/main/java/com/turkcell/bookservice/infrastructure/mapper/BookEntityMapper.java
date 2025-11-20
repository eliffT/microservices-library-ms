package com.turkcell.bookservice.infrastructure.mapper;

import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.book.Book;
import com.turkcell.bookservice.domain.model.book.Isbn;
import com.turkcell.bookservice.infrastructure.persistence.entity.AuthorEntity;
import com.turkcell.bookservice.infrastructure.persistence.entity.BookEntity;
import com.turkcell.bookservice.infrastructure.persistence.entity.CategoryEntity;
import com.turkcell.bookservice.infrastructure.persistence.entity.PublisherEntity;
import org.springframework.stereotype.Component;

@Component
public class BookEntityMapper {
    public BookEntity toEntity(Book book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(book.id().value());

        bookEntity.setPrice(book.price());
        bookEntity.setIsbn(book.isbn().value());
        bookEntity.setTitle(book.title());
        bookEntity.setYear(book.year());
        bookEntity.setLanguage(book.language());
        bookEntity.setTotalCopies(book.totalCopies());
        bookEntity.setAvailableCopies(book.availableCopies());
        bookEntity.setStatus(book.status());

        if (book.authorId() != null) {
            bookEntity.setAuthor(new AuthorEntity(book.authorId().value()));
        }
        if (book.publisherId() != null) {
            bookEntity.setPublisher(new PublisherEntity(book.publisherId().value()));
        }
        if (book.categoryId() != null) {
            bookEntity.setCategory(new CategoryEntity(book.categoryId().value()));
        }

        return bookEntity;
    }

    public Book toDomain(BookEntity entity) {
        return Book.rehydrate(
                new DomainId<>(entity.id()),
                new Isbn(entity.isbn()),
                entity.title(),
                entity.year(),
                entity.language(),
                entity.totalCopies(),
                entity.availableCopies(),
                entity.status(),
                entity.author() == null ? null : new DomainId<>(entity.author().id()),
                entity.publisher() == null ? null: new DomainId<>(entity.publisher().id()),
                entity.category() == null ? null : new DomainId<>(entity.category().id()),
                entity.price()
        );
    }

}