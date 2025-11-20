package com.turkcell.bookservice.application.query.handler;

import com.turkcell.bookservice.application.dto.AuthorResponse;
import com.turkcell.bookservice.application.mapper.AuthorResponseMapper;
import com.turkcell.bookservice.application.query.ListAuthorsQuery;
import com.turkcell.bookservice.core.cqrs.QueryHandler;
import com.turkcell.bookservice.domain.repository.AuthorRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListAuthorsQueryHandler implements QueryHandler<ListAuthorsQuery, List<AuthorResponse>> {

    private final AuthorRepository authorRepository;
    private final AuthorResponseMapper authorResponseMapper;

    public ListAuthorsQueryHandler(AuthorRepository authorRepository, AuthorResponseMapper authorResponseMapper) {
        this.authorRepository = authorRepository;
        this.authorResponseMapper = authorResponseMapper;
    }

    @Override
    public List<AuthorResponse> handle(ListAuthorsQuery query) {
        return authorRepository.findAll()
                .stream()
                .map(authorResponseMapper::toResponse)
                .toList();
    }
}