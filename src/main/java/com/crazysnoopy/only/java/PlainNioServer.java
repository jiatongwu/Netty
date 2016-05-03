package com.crazysnoopy.only.java;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by wonhwa on 2016-05-03.
 */
public class PlainNioServer {
    public void serve(int port) throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket serverSocket = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address);
        // 서버를 선택된 포트로 바인딩
        Selector selector = Selector.open();
        // 채널을 처리할 셀렉터를 염
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 연결을 수락할 ServerSocket 을 셀렉터에 등록
        final ByteBuffer message = ByteBuffer.wrap("Hi!\r\n".getBytes());

        for (;;) {
            try {
                selector.select();
                // 처리할 새로운 이벤트를 기다리고 다음 들어오는 이벤트까지 블로킹
            } catch (IOException ex) {
                ex.printStackTrace();
                // 예외처리
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            // 이벤트를 수신한 모든 SelectionKey 인스턴스를 얻음
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {
                        // 이벤트가 수락할 수 있는 새로운 연결인지 확인
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, message.duplicate());
                        // 클라이언트를 수락하고 셀렉터에 등록
                        System.out.println(
                                "Accepted connection from " + client
                        );
                    }

                    if (key.isWritable()) {
                        // 소켓에 데이터를 기록할 수 있는지 확인
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {
                                // 연결된 클라이언트로 데이터를 출력
                                break;
                            }
                        }
                        client.close();
                        // 연결 닫음
                    }
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException ex) {

                    }
                }
            }
        }
    }
}
