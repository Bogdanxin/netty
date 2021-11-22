package io.netty.example.mytest.lagou.c8;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author weixingong.gwx
 * @date 2021/11/18
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Receive client: [" + ((ByteBuf) msg).toString(CharsetUtil.UTF_8) + "]");
	}
}
