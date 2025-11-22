package com.turkcell.bookservice.application.commands.handler;

import com.turkcell.bookservice.application.commands.CreateAuthorCommand;
import com.turkcell.bookservice.application.dto.CreatedAuthorResponse;
import com.turkcell.bookservice.application.mapper.CreateAuthorMapper;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.domain.model.author.Author;
import com.turkcell.bookservice.domain.repository.AuthorRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateAuthorCommandHandler implements CommandHandler<CreateAuthorCommand, CreatedAuthorResponse> {
    private final AuthorRepository authorRepository;
    private final CreateAuthorMapper mapper;

    public CreateAuthorCommandHandler(AuthorRepository authorRepository,
                                      CreateAuthorMapper mapper) {
        this.authorRepository = authorRepository;
        this.mapper = mapper;
    }

    @Override
    public CreatedAuthorResponse handle(CreateAuthorCommand command) {
        Author author = mapper.toDomain(command);
        author = authorRepository.save(author);
        return mapper.toResponse(author);
    }
}
