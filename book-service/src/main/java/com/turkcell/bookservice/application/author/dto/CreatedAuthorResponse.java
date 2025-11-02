package com.turkcell.bookservice.application.author.dto;

import java.util.UUID;

public record CreatedAuthorResponse(UUID id, String fullName) {
}

