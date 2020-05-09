package org.example.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.handler.AcceptHandler;
import org.example.handler.Handler;
import org.example.handler.ReadHandler;
import org.example.handler.WriteHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Slf4j
public class SingleThreadedSelectorNonBlockingServer {
  @SneakyThrows
  public static void main(String[] args) {
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(8080));
    ssc.configureBlocking(false);
    Selector selector = Selector.open();
    ssc.register(selector, SelectionKey.OP_ACCEPT);

    Map<SocketChannel, Queue<ByteBuffer>> pendingData = new HashMap<>();
    Handler<SelectionKey> acceptHandler = new AcceptHandler(pendingData);
    Handler<SelectionKey> readHandler = new ReadHandler(pendingData);
    Handler<SelectionKey> writeHandler = new WriteHandler(pendingData);

    while (true) {
      selector.select();
      Set<SelectionKey> keys = selector.selectedKeys();
      for (Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext(); ) {
        SelectionKey key = iterator.next();
        iterator.remove();
        if (key.isAcceptable()) {
          acceptHandler.handle(key);
        } else if (key.isReadable()) {
          readHandler.handle(key);
        } else if (key.isWritable()) {
          writeHandler.handle(key);
        }
      }
    }
  }
}
