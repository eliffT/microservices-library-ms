package com.turkcell.bookservice.application.commands;

import com.turkcell.bookservice.application.dto.CreatedAuthorResponse;
import com.turkcell.bookservice.core.cqrs.Command;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAuthorCommand(
        @NotBlank @Size(min=6,max=255) String fullName)
        implements Command<CreatedAuthorResponse> {

}

