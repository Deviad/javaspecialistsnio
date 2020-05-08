package org.example.util;

import lombok.experimental.UtilityClass;

import java.nio.ByteBuffer;

@UtilityClass
public class Util {
  public int transmogrify(int data) {
    return Character.isLetter(data) ? data ^ ' ' : data;
  }
  public void transmogrify(ByteBuffer buff) {
    buff.flip();
    for (int i=0; i<buff.limit(); i++) {
      buff.put(i, (byte) transmogrify(buff.get(i)));

    }
  }
}
