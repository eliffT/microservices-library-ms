package com.turkcell.bookservice.application.query.handler;

import com.turkcell.bookservice.application.dto.BookResponse;
import com.turkcell.bookservice.application.exceptions.NotFoundException;
import com.turkcell.bookservice.application.mapper.BookResponseMapper;
import com.turkcell.bookservice.application.query.GetBookByIdQuery;
import com.turkcell.bookservice.core.cqrs.QueryHandler;
import com.turkcell.bookservice.domain.model.DomainId;
import com.turkcell.bookservice.domain.model.book.Book;
import com.turkcell.bookservice.domain.repository.BookRepository;
import org.springframework.stereotype.Component;

@Component
public class GetBookByIdQueryHandler implements QueryHandler<GetBookByIdQuery, BookResponse> {

    private final BookRepository bookRepository;
    private final BookResponseMapper bookResponseMapper;

    public GetBookByIdQueryHandler(BookRepository bookRepository, BookResponseMapper bookResponseMapper) {
        this.bookRepository = bookRepository;
        this.bookResponseMapper = bookResponseMapper;
    }
    @Override
    public BookResponse handle(GetBookByIdQuery query) {
        Book book = bookRepository.findById(DomainId.from(query.bookId()))
                .orElseThrow(() -> new NotFoundException("Book not found with ID: " + query.bookId()));

        return bookResponseMapper.toResponse(book);
    }
}
