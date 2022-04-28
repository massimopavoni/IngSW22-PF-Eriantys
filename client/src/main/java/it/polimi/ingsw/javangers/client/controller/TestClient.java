package it.polimi.ingsw.javangers.client.launcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class TestClient {
    private final Socket socket;
    private Scanner socketIn;
    private PrintWriter socketOut;
    private Scanner stdin;

    public TestClient(int port) {
        try {
            socket = new Socket("127.0.0.1", port);
            System.out.println("Connection established");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        TestClient cl = new TestClient(50666);
        cl.socketIn = new Scanner(cl.socket.getInputStream());
        cl.socketOut = new PrintWriter(cl.socket.getOutputStream());
        cl.stdin = new Scanner(System.in);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                cl.socketOut.println("exit");
                cl.socketOut.flush();
                cl.socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
        new Thread(() -> {
            while (cl.socket.isConnected()) {
                String inputLine = cl.stdin.nextLine();
                cl.socketOut.println(inputLine);
                cl.socketOut.flush();
            }
            Thread.currentThread().interrupt();
        }).start();
        new Thread(() -> {
            while (cl.socket.isConnected()) {
                try {
                    String socketLine = cl.socketIn.nextLine();
                    System.out.println(socketLine);
                } catch (NoSuchElementException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}

