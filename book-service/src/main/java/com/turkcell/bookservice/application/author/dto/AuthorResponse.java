package com.turkcell.bookservice.application.author.dto;

import java.util.UUID;

public record AuthorResponse(UUID id, String fullName) {

}