package com.crazysnoopy.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by wonhwa on 2016-05-02.
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

    /*
        Todo
        1. 서버로 연결한다.
        2. 메시지를 하나 이상 전송한다.
        3. 메시지마다 대기하고, 서버로부터 동일한 메시지를 수신한다.
        4. 연결을 닫는다.

        // channelActive() : 서버에 대한 연결이 만들어지면 호출된다.
        // channelRead0() : 서버로부터 메시지를 수신하면 호출된다.
        // exceptionCaught() : 처리 중에 예외가 발생하면 호출된다.

     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        // 채널 활성화 알림을 받으면 메세지를 전송
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println(
        "Client received : " + byteBuf.toString(CharsetUtil.UTF_8)
        );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
