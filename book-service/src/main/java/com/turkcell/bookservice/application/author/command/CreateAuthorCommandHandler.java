package com.turkcell.bookservice.application.author.command;

import com.turkcell.bookservice.application.author.dto.CreatedAuthorResponse;
import com.turkcell.bookservice.application.author.mapper.CreateAuthorMapper;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.domain.model.author.Author;
import com.turkcell.bookservice.domain.repository.AuthorRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateAuthorCommandHandler implements CommandHandler<CreateAuthorCommand, CreatedAuthorResponse> {
    private final AuthorRepository authorRepository;
    private final CreateAuthorMapper createAuthorMapper;

    public CreateAuthorCommandHandler(AuthorRepository authorRepository, CreateAuthorMapper createAuthorMapper) {
        this.authorRepository = authorRepository;
        this.createAuthorMapper = createAuthorMapper;
    }

    @Override
    public CreatedAuthorResponse handle(CreateAuthorCommand command) {
        Author author = createAuthorMapper.toDomain(command);
        author = authorRepository.save(author);
        return createAuthorMapper.toResponse(author);
    }
}
