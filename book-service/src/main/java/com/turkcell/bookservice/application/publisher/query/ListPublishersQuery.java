package com.turkcell.bookservice.application.publisher.query;

import com.turkcell.bookservice.application.publisher.dto.PublisherResponse;
import com.turkcell.bookservice.core.cqrs.Query;
import jakarta.validation.constraints.Min;

import java.util.List;

public record ListPublishersQuery(
        @Min(0) Integer pageIndex,
        @Min(1) Integer pageSize
) implements Query<List<PublisherResponse>> {}
