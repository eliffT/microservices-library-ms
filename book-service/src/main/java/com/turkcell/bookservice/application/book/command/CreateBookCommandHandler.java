package com.turkcell.bookservice.application.book.command;

import com.turkcell.bookservice.application.book.dto.CreatedBookResponse;
import com.turkcell.bookservice.application.book.mapper.CreateBookMapper;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.domain.model.book.Book;
import com.turkcell.bookservice.domain.repository.BookRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateBookCommandHandler implements CommandHandler<CreateBookCommand, CreatedBookResponse> {

    private final BookRepository bookRepository;
    private final CreateBookMapper createBookMapper;

    public CreateBookCommandHandler(BookRepository bookRepository, CreateBookMapper createBookMapper) {
        this.bookRepository = bookRepository;
        this.createBookMapper = createBookMapper;
    }

    @Override
    public CreatedBookResponse handle(CreateBookCommand command) {
        Book book = createBookMapper.toDomain(command);
        book = bookRepository.save(book);
        return createBookMapper.toResponse(book);
    }
}
