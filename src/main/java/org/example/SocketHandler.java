package org.example;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

@Slf4j
@UtilityClass
public class SocketHandler {
    public BiConsumer<Socket, UnaryOperator<Integer>> HANDLE = (s, transmogrify)-> {
        try (s; InputStream in = s.getInputStream(); OutputStream out = s.getOutputStream()) {
            int data;
            in.transferTo(out);
            while ((data = in.read()) != -1) {
                log.info("test: {}", data);
                out.write(transmogrify.apply(data));
                log.info("test trans: {}", transmogrify.apply(data));
            }
        } catch (IOException ex) {
            log.info(ex.getMessage());
            throw new UncheckedIOException(ex);
        }
    };

    public int transmogrify(int data) {
        return Character.isLetter(data) ? data ^ ' ': data;
    }

}
