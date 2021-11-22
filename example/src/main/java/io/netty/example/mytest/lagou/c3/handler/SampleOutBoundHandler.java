package io.netty.example.mytest.lagou.c3.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * @author weixingong.gwx
 * @date 2021/11/17
 */
public class SampleOutBoundHandler extends ChannelOutboundHandlerAdapter {

	private final String name;

	public SampleOutBoundHandler(String name) {
		this.name = name;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("OutBoundHandler: " + name);
		super.write(ctx, msg, promise);
	}
}
