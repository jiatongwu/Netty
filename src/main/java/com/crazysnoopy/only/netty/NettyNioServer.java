package com.crazysnoopy.only.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by wonhwa on 2016-05-03.
 */
public class NettyNioServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));
        EventLoopGroup group = new NioEventLoopGroup();
        // 논블로킹 모드를 위해 NioEventLoopGroup을 이용
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // ServerBootstrap을 생성
            serverBootstrap.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 연결이 수락될 때마다 호출될 ChannelInitializer를 지정
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter(){
                                        // 이벤트를 수신하고 처리할 ChannelInboudHander Adapter를 추가
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                            ctx.writeAndFlush(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
                                            // 클라이언트로 메세지를 출력하고 ChannelFutureListener를 추가해 메세지가 출력되면 연결을 닫음
                                        }
                                    }
                            );
                        }
                    });
            ChannelFuture future = serverBootstrap.bind().sync();
            // 서버를 바인딩해 연결을 수락
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
            // 모든 리소스를 해제
        }
    }
}
