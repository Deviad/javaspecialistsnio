package org.example.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.util.Util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

@Slf4j
@RequiredArgsConstructor
public class PoolReadHandler implements Handler<SelectionKey> {
  private final ExecutorService pool;
  private final Queue<Runnable> selectorActions;
  private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

  @Override
  public void handle(SelectionKey selectionKey) throws IOException {
    SocketChannel sc = (SocketChannel) selectionKey.channel();
    ByteBuffer buf = ByteBuffer.allocateDirect(80);
    int read = sc.read(buf);
    if (read == -1) {
      pendingData.remove(sc);
      sc.close();
      log.info("Disconnected from " + sc + "( in pool read())");
      return;
    }
    if (read > 0) {
      pool.submit(
          () -> {
            Util.transmogrify(buf);
            pendingData.get(sc).add(buf);
            selectorActions.add(()->selectionKey.interestOps(SelectionKey.OP_WRITE));
            selectionKey.selector().wakeup();
          });
    }
  }
}
