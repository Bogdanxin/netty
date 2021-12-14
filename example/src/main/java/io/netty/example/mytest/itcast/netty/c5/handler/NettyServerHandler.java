package io.netty.example.mytest.itcast.netty.c5.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author weixingong.gwx
 * @date 2021/11/24
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		String str = buf.toString(Charset.defaultCharset());

		log.debug("服务器获取数据: {}", str);
		log.debug("服务器写出数据...");

		ByteBuf out = getByteBuf(ctx, str);
		ctx.channel().writeAndFlush(out);
	}

	private ByteBuf getByteBuf(ChannelHandlerContext ctx, String msg) {
		byte[] bytes = ("服务器发送的数据" + msg).getBytes(StandardCharsets.UTF_8);
		ByteBuf buffer = ctx.alloc().buffer();
		buffer.writeBytes(buffer);
		return buffer;
	}
}
