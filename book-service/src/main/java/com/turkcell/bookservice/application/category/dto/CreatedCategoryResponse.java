package com.turkcell.bookservice.application.category.dto;

import java.util.UUID;

public record CreatedCategoryResponse(UUID id, String name, String description) { }
