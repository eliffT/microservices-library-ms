package com.turkcell.bookservice.core.cqrs;

public interface CommandHandler<C extends Command<R>, R> {
    R handle(C command);
}
