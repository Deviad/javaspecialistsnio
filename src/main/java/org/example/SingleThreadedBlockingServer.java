package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */

@Slf4j
public class SingleThreadedBlockingServer
{
    @SneakyThrows
    public static void main(String[] args )
    {
        ServerSocket ss = new ServerSocket(8080);
        while(true) {

            Socket s = ss.accept();

            SocketHandler.HANDLE.accept(s, SocketHandler::transmogrify);
        }

    }
}
