package org.example.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@UtilityClass
@Slf4j
public class Util {
  public int transmogrify(int data) {
    return Character.isLetter(data) ? data ^ ' ' : data;
  }
  public void transmogrify(ByteBuffer buff) {
    log.info("Transmogrification done by " + Thread.currentThread());
    buff.flip();
    for (int i=0; i<buff.limit(); i++) {
      buff.put(i, (byte) transmogrify(buff.get(i)));

    }
  }
}
