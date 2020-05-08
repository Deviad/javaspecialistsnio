package org.example.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.handler.Handler;
import org.example.handler.PrintingHandler;
import org.example.handler.TransmogrifyChannelHandler;
import org.example.handler.TransmogrifyHandler;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;

/** Hello world! */
@Slf4j
public class SingleThreadedPollingNonBlockingServer {
  @SneakyThrows
  public static void main(String[] args) {
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(8080));
    ssc.configureBlocking(false);

    Handler<SocketChannel> handler = new TransmogrifyChannelHandler();
    Collection<SocketChannel> sockets = new ArrayList<>();
    while (true) {
      SocketChannel sc = ssc.accept();
      if(sc != null) {
        sockets.add(sc);
        log.info("Connected to " + sc);
        sc.configureBlocking(false);
      }
      for (SocketChannel socket : sockets) {
          if(socket.isConnected()) {
            handler.handle(socket);
          }
      }
      sockets.removeIf(socketChannel -> !socketChannel.isConnected());
    }
  }
}
