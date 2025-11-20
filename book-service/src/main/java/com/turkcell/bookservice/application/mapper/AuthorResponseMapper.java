package com.turkcell.bookservice.application.mapper;

import com.turkcell.bookservice.application.dto.AuthorResponse;
import com.turkcell.bookservice.domain.model.author.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorResponseMapper {
    public AuthorResponse toResponse(Author author){
        return  new AuthorResponse(author.id().value(), author.fullName());
    }
}
