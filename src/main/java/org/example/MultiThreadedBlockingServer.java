package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class MultiThreadedBlockingServer {
    @SneakyThrows
    public static void main(String[] args) {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket s = ss.accept();
            new Thread(() -> SocketHandler.HANDLE.accept(s, SocketHandler::transmogrify)).start();
        }

    }

}
