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

@RequiredArgsConstructor
@Slf4j
public class ReadHandler implements Handler<SelectionKey> {
    private final Map<SocketChannel, Queue<ByteBuffer>> pendingData;
    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel sc = (SocketChannel) selectionKey.channel();
        ByteBuffer buf = ByteBuffer.allocateDirect(80);
        int read = sc.read(buf);
        if (read == -1) {
            pendingData.remove(sc);
            sc.close();
            log.info("Disconnected from " + sc + "( in read())");
            return;
        }
        if (read>0) {
            Util.transmogrify(buf);
            pendingData.get(sc).add(buf);
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }

    }
}
