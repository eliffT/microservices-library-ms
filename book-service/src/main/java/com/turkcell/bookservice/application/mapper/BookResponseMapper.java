package com.turkcell.bookservice.application.mapper;

import com.turkcell.bookservice.application.dto.BookResponse;
import com.turkcell.bookservice.domain.model.book.Book;
import org.springframework.stereotype.Component;

@Component
public class BookResponseMapper {

    public BookResponse toResponse(Book book){
        return new BookResponse(book.id().value(), book.authorId().value(), book.categoryId().value(), book.publisherId().value(),
                book.title(), book.year(), book.language(), book.totalCopies(),
                book.price());
    }
}
