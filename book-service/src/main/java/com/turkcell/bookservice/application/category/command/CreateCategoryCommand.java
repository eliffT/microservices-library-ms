package com.turkcell.bookservice.application.category.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryCommand(
        @NotBlank @Size(min = 2, max = 100) String name,
        @NotBlank @Size(min = 2, max = 255) String description)
        implements Command<CreatedCategoryResponse> { }