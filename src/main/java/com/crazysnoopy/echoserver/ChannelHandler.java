package com.crazysnoopy.echoserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by wonhwa on 2016-05-02.
 */
public class ChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        System.out.println(
                "Client " + ctx.channel().remoteAddress() + " connected"
        );
    }
}
