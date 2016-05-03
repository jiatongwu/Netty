package com.crazysnoopy.only.java;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by wonhwa on 2016-05-03.
 */
public class PlainOioServer {
    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);
        // Allocate Port To Socket
        try {
            for (; ; ) {
                final Socket clientSocket = socket.accept();
                // Connection Accept

                System.out.println(
                        "Accepted connection from " + clientSocket
                );

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream outputStream;
                        try {
                            outputStream = clientSocket.getOutputStream();
                            outputStream.write("Hi!\r\n".getBytes(
                                    Charset.forName("UTF-8")
                            ));
                            // Send Message To Client
                            outputStream.flush();
                            clientSocket.close();
                            // Connection Close
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                // Start Thread
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
