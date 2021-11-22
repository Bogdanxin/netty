package io.netty.example.mytest.lagou.c9.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @author weixingong.gwx
 * @date 2021/11/18
 */
public class RequestSampleHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
		ResponseSample response = new ResponseSample("ok", data, System.currentTimeMillis());
		ctx.channel().writeAndFlush(response);
	}
}
