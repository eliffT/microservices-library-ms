package com.turkcell.bookservice.interfaces.web;

import com.turkcell.bookservice.application.publisher.command.CreatePublisherCommand;
import com.turkcell.bookservice.application.publisher.dto.CreatedPublisherResponse;
import com.turkcell.bookservice.application.publisher.dto.PublisherResponse;
import com.turkcell.bookservice.application.publisher.query.ListPublishersQuery;
import com.turkcell.bookservice.core.cqrs.CommandHandler;
import com.turkcell.bookservice.core.cqrs.QueryHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/publishers")
@Validated
public class PublishersController {
    private final QueryHandler<ListPublishersQuery, List<PublisherResponse>> listPublishersQueryHandler;
    private final CommandHandler<CreatePublisherCommand, CreatedPublisherResponse> createPublisherCommandHandler;

    public PublishersController(
            QueryHandler<ListPublishersQuery, List<PublisherResponse>> listPublishersQueryHandler,
            CommandHandler<CreatePublisherCommand, CreatedPublisherResponse> createPublisherCommandHandler)
    {
        this.listPublishersQueryHandler = listPublishersQueryHandler;
        this.createPublisherCommandHandler = createPublisherCommandHandler;
    }

    @GetMapping
    public List<PublisherResponse> getPublishers(@Valid ListPublishersQuery query) {
        return listPublishersQueryHandler.handle(query);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedPublisherResponse createPublisher(@Valid @RequestBody CreatePublisherCommand command) {
        return createPublisherCommandHandler.handle(command);
    }
}