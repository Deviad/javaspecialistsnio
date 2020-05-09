package org.example.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.handler.AcceptHandler;
import org.example.handler.Handler;
import org.example.handler.PoolReadHandler;
import org.example.handler.WriteHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SelectorWithWorkPoolNonBlockingServer {
  @SneakyThrows
  public static void main(String[] args) {
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(8080));
    ssc.configureBlocking(false);
    Selector selector = Selector.open();
    ssc.register(selector, SelectionKey.OP_ACCEPT);

    ExecutorService pool = Executors.newFixedThreadPool(10);
    Map<SocketChannel, Queue<ByteBuffer>> pendingData = new ConcurrentHashMap<>();
    Queue<Runnable> selectorActions = new ConcurrentLinkedQueue<>();

    Handler<SelectionKey> acceptHandler = new AcceptHandler(pendingData);
    Handler<SelectionKey> readHandler = new PoolReadHandler(pool, selectorActions, pendingData);
    Handler<SelectionKey> writeHandler = new WriteHandler(pendingData);

    while (true) {
      selector.select();
      processSelectorActions(selectorActions);
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

  public static void processSelectorActions(Queue<Runnable> selectorActions) {
    Runnable action;
    while ((action = selectorActions.poll()) != null) {
      action.run();
    }
  }
}
