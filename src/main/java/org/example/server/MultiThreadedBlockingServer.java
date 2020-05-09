package org.example.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.handler.Handler;
import org.example.handler.PrintingHandler;
import org.example.handler.ThreadedHandler;
import org.example.handler.TransmogrifyHandler;
import org.example.handler.UncheckedIOExceptionConverterHandler;

import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class MultiThreadedBlockingServer {
  @SneakyThrows
  public static void main(String[] args) {
    ServerSocket ss = new ServerSocket(8080);
    Handler<Socket> handler =
        new ThreadedHandler<>(
            new UncheckedIOExceptionConverterHandler<>(
                new PrintingHandler<>(new TransmogrifyHandler())));

    while (true) {
      Socket s = ss.accept();
      handler.handle(s);
    }
  }
}
