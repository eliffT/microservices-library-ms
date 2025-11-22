package com.turkcell.bookservice.application.mapper;

import com.turkcell.bookservice.application.commands.CreateBookCommand;
import com.turkcell.bookservice.application.dto.CreatedBookResponse;
import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.book.Book;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CreateBookMapper {

    public Book toDomain(CreateBookCommand command){
        return Book.create(command.title(), command.year(), command.language(), command.totalCopies(),
                DomainId.from(UUID.fromString(command.authorId())),
                DomainId.from(UUID.fromString(command.publisherId())),
                DomainId.from(UUID.fromString(command.categoryId())), command.price());
    }

    public CreatedBookResponse toResponse(Book book){
        return new CreatedBookResponse(book.id().value(), book.authorId().value(), book.categoryId().value(),
                book.publisherId().value(), book.title(), book.year(), book.language(),
                book.totalCopies(), book.price());
    }
}
