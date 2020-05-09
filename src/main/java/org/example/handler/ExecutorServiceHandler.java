package org.example.handler;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

@Slf4j
public class ExecutorServiceHandler<S> extends DecoratedHandler<S> {

  private final ExecutorService pool;
  private final Thread.UncaughtExceptionHandler exceptionHandler;

  public ExecutorServiceHandler(
      Handler<S> other, ExecutorService pool, Thread.UncaughtExceptionHandler exceptionHandler) {
    super(other);
    this.pool = pool;
    this.exceptionHandler = exceptionHandler;
  }

  public ExecutorServiceHandler(Handler<S> other, ExecutorService pool) {
    this(other, pool, (t, e) -> log.info("uncaught: " + t + " error " + e));
  }

  @Override
  public void handle(S s) {
    pool.submit(
        new FutureTask<>(
            () -> {
              super.handle(s);
              return null;
            }) {
          @Override
          protected void setException(Throwable t) {
            exceptionHandler.uncaughtException(Thread.currentThread(), t);
          }
        });
  }
}
