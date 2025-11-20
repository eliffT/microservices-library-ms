package com.turkcell.bookservice.application.query.handler;

import com.turkcell.bookservice.application.dto.BookResponse;
import com.turkcell.bookservice.application.mapper.BookResponseMapper;
import com.turkcell.bookservice.application.query.ListBooksQuery;
import com.turkcell.bookservice.core.cqrs.QueryHandler;
import com.turkcell.bookservice.domain.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListBooksQueryHandler implements QueryHandler<ListBooksQuery, List<BookResponse>> {

    private final BookRepository bookRepository;
    private final BookResponseMapper bookResponseMapper;

    public ListBooksQueryHandler(BookRepository bookRepository, BookResponseMapper bookResponseMapper) {
        this.bookRepository = bookRepository;
        this.bookResponseMapper = bookResponseMapper;
    }

    @Override
    public List<BookResponse> handle(ListBooksQuery query) {
        return bookRepository.findAll().stream().map(bookResponseMapper::toResponse).toList();
    }

}
