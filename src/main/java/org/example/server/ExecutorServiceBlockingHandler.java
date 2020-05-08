package org.example.server;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.handler.ExecutorServiceHandler;
import org.example.handler.Handler;
import org.example.handler.PrintingHandler;
import org.example.handler.TransmogrifyHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class ExecutorServiceBlockingHandler {
    @SneakyThrows
    public static void main(String[] args) {
        ServerSocket ss = new ServerSocket(8080);
    Handler<Socket> handler =
        new ExecutorServiceHandler<>(
            new PrintingHandler<>(new TransmogrifyHandler()),
            Executors.newFixedThreadPool(10));

        while (true) {
            Socket s = ss.accept();
            handler.handle(s);
        }
    }
}
