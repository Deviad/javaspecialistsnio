package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
public class NastyChump {
  @SneakyThrows
  public static void main(String[] args) {
    Socket[] socket = new Socket[20000];
    for (int i = 0; i < socket.length; i++) {
      try {
        socket[i] = new Socket("localhost", 8080);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    Thread.sleep(100_000);
  }
}
