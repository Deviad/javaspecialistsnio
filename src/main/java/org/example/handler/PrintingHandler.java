package org.example.handler;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class PrintingHandler<S> extends DecoratedHandler<S> {

  public PrintingHandler(Handler<S> other) {
    super(other);
  }

  @Override
  public void handle(S s) throws IOException {
    log.info("Connected to " + s);
    try {
      super.handle(s);
    } finally {
      log.info("Disconnected from " + s);
    }
  }
}
