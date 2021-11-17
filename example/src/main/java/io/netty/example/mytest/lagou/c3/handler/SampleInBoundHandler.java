package io.netty.example.mytest.lagou.c3.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author weixingong.gwx
 * @date 2021/11/17
 */
public class SampleInBoundHandler extends ChannelInboundHandlerAdapter {

	private final String name;
	private final boolean flush;

	public SampleInBoundHandler(String name, boolean flush) {
		this.name = name;
		this.flush = flush;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("InBoundHandler: " + name);
		if (flush) {
			ctx.channel().writeAndFlush(msg);
		} else {
			super.channelRead(ctx, msg);
		}
	}
}
