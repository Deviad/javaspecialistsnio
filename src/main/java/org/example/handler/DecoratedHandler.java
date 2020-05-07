package org.example.handler;


import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class DecoratedHandler<S> implements Handler<S> {
    private final Handler<S> other;

    @Override
    public void handle(S s) throws IOException {
        other.handle(s);
    }
}
