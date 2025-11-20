package com.turkcell.bookservice.application.dto;

import java.util.UUID;

public record CreatedAuthorResponse(UUID id, String fullName) {
}

