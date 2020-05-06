package org.example;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Slf4j
public class ConsoleInputStream {
    @SneakyThrows
    public static void main(String[] args) {

        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(in);
        try (br; in) {
            String line;
            while ((line = br.readLine()) != null) {
                log.info(line.chars().mapToObj(c-> (char)c).map(Object::toString).collect(Collectors.joining()));
            }
        } catch(IOException ex ) {
            log.info(ex.getMessage());
        }
    }
}
