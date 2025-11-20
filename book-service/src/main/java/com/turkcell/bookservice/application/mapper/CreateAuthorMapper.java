package com.turkcell.bookservice.application.mapper;

import com.turkcell.bookservice.application.commands.CreateAuthorCommand;
import com.turkcell.bookservice.application.dto.CreatedAuthorResponse;
import com.turkcell.bookservice.domain.model.author.Author;
import org.springframework.stereotype.Component;

@Component
public class CreateAuthorMapper {
    public Author toDomain(CreateAuthorCommand command){
        return Author.create(command.fullName());
    }

    public CreatedAuthorResponse toResponse(Author author){
        return new CreatedAuthorResponse(author.id().value(), author.fullName());
    }
}

