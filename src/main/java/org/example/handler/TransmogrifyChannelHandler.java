package org.example.handler;

import org.example.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TransmogrifyChannelHandler implements Handler<SocketChannel> {

  @Override
  public void handle(SocketChannel sc) throws IOException {
    ByteBuffer buff = ByteBuffer.allocateDirect(80);
    int read = sc.read(buff);
    if(read == -1) {
      sc.close();
      return;
    }
    if (read> 0) {
       Util.transmogrify(buff);
       while(buff.hasRemaining()) {
         sc.write(buff);
       }
       buff.compact();

    }
  }
}
