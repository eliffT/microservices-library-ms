package com.turkcell.bookservice.interfaces.web;


import com.turkcell.bookservice.application.dto.BookResponse;

import com.turkcell.bookservice.application.dto.CreatedBookResponse;

import com.turkcell.bookservice.application.query.ListBooksQuery;
import com.turkcell.bookservice.application.commands.CreateBookCommand;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.core.cqrs.QueryHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@Validated
public class BooksController {

    private final QueryHandler<ListBooksQuery, List<BookResponse>> listBooksQueryListQueryHandler;
    private final CommandHandler<CreateBookCommand, CreatedBookResponse> createBookComamndHandler;

    public BooksController(QueryHandler<ListBooksQuery, List<BookResponse>> listBooksQueryListQueryHandler, CommandHandler<CreateBookCommand, CreatedBookResponse> createBookComamndHandler) {
        this.listBooksQueryListQueryHandler = listBooksQueryListQueryHandler;
        this.createBookComamndHandler = createBookComamndHandler;
    }

    @GetMapping
    public List<BookResponse> getBooks(@Valid ListBooksQuery query)
    {
        return listBooksQueryListQueryHandler.handle(query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedBookResponse createBook(@Valid @RequestBody CreateBookCommand command){
        return createBookComamndHandler.handle(command);
    }



}
