package org.example.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Queue;

@RequiredArgsConstructor
@Slf4j
public class WriteHandler implements Handler<SelectionKey> {
  private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;

  @Override
  public void handle(SelectionKey selectionKey) throws IOException {
    SocketChannel sc = (SocketChannel) selectionKey.channel();
    Queue<ByteBuffer> queue = pendingData.get(sc);
    while (!queue.isEmpty()) {
      ByteBuffer buf = queue.peek();
      int written = sc.write(buf);
      if (written == -1) {
        sc.close();
        log.info("Disconnected from " + sc + "( in write())");
        pendingData.remove(sc);
        return;
      }
      if (buf.hasRemaining()) {
        return;
      }
      queue.remove();
    }
    selectionKey.interestOps(SelectionKey.OP_READ);
  }
}
