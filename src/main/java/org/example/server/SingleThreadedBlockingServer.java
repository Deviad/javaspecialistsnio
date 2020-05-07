package org.example.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.handler.Handler;
import org.example.handler.PrintingHandler;
import org.example.handler.TransmogrifyHandler;

import java.net.ServerSocket;
import java.net.Socket;

/** Hello world! */
@Slf4j
public class SingleThreadedBlockingServer {
  @SneakyThrows
  public static void main(String[] args) {
    ServerSocket ss = new ServerSocket(8080);

    Handler<Socket> handler = new PrintingHandler<>(new TransmogrifyHandler());

    while (true) {
      Socket s = ss.accept();
      handler.handle(s);
    }
  }
}
