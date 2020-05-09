package org.example.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.handler.BlockingChannelHandler;
import org.example.handler.ExecutorServiceHandler;
import org.example.handler.Handler;
import org.example.handler.PrintingHandler;
import org.example.handler.TransmogrifyChannelHandler;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

@Slf4j
public class BlockingNIOServer {
  @SneakyThrows
  public static void main(String[] args) {
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(8080));

    Handler<SocketChannel> handler =
        new ExecutorServiceHandler<>(
            new PrintingHandler<>(new BlockingChannelHandler(new TransmogrifyChannelHandler())),
            Executors.newFixedThreadPool(10));

    while (true) {
      SocketChannel s = ssc.accept();
      handler.handle(s);
    }
  }
}
