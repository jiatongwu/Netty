package com.crazysnoopy.echoserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by wonhwa on 2016-05-02.
 */
public class EchoServer {

    /*
        서버가 수신할 포트를 바인딩하고 들어오는 연결 요청을 수락한다.
        EchoServerHandler 인스턴스에 인바운드 메세지에 대해 알리도록 Channel을 구성한다.

        부트스트랩 단계
        1. 서버를 부트스트랩하고 바인딩하는 데 이용할 ServerBootstrap 인스턴스 생성
        2. 새로운 연결 수락 및 데이터 읽기/쓰기와 같은 이벤트 처리를 수행할 NioEventLoopGroup 인스턴스를 생성하고 할당.
        3. 서버가 바인딩하는 로컬 InetSocketAddress를 지정
        4. EchoServerHandler 인스턴스를 이용해 새로운 각  Channel을 초기화 함
        5. ServerBootstrap.bind()를 호출해 서버를 바인딩.
     */

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.err.println(
                    "Usage: " + EchoServer.class.getSimpleName() + " <port>"
            );
            int port = Integer.parseInt(args[0]);
            // 포트 값을 설정(포트 인수 형식이 잘못된 경우 NumberFormatException을 발생시킴)
            new EchoServer(port).start();
            // 서버의 시작 메서드 호출
        }
    }

    private void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        // EventLoopGroup을 생성
        try {
            ServerBootstrap b = new ServerBootstrap();
            // ServerBootstrap 생성
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    // NIO 전송 채널을 이용하도록 지정
                    .localAddress(new InetSocketAddress(port))
                    // 지정된 포트를 이용해 소켓 주소를 설정
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // EchoServerHandler 하나를 채널의 Channel Pipeline으로 추가
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(serverHandler);
                            // EchoServerHandler는 @Sharable이므로 동일한 항목을 이용할 수 있음
                        }
                    });
            ChannelFuture f = b.bind().sync();
            // 서버를 비동기식으로 바인딩, sync()는 바인딩이 완료되기를 대기
            f.channel().closeFuture().sync();
            // 채널의 CloseFuture를 얻고 완료될 때까지 현재 스레드를 블로킹
        } finally {
            group.shutdownGracefully().sync();
            // EventLoopGroup을 종료하고 모든 리소스를 해제
        }
    }
}
