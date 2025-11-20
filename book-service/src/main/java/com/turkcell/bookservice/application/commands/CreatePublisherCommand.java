package com.turkcell.bookservice.application.commands;

import com.turkcell.bookservice.application.dto.CreatedPublisherResponse;
import com.turkcell.bookservice.core.cqrs.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePublisherCommand(
        @NotBlank @Size(min = 2, max = 255) String name)
        implements Command<CreatedPublisherResponse> { }

