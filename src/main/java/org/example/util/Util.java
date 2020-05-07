package org.example.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {
  public int transmogrify(int data) {
    return Character.isLetter(data) ? data ^ ' ' : data;
  }
}
