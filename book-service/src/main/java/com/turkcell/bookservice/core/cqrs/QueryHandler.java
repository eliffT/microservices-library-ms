package com.turkcell.bookservice.core.cqrs;

public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
}