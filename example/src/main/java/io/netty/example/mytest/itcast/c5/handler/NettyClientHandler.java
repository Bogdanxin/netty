package io.netty.example.mytest.itcast.c5.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author weixingong.gwx
 * @date 2021/11/24
 */
@Slf4j
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
//	@Override
//	public void channelActive(ChannelHandlerContext ctx) throws Exception {
//		log.debug("客户端写出数据...");
//
//	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		log.debug("客户端读取数据: {}", msg.toString());
	}

//	private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
//		// 1. 获取二进制抽象 ByteBuf
//		ByteBuf buffer = ctx.alloc().buffer();
//		Random random = new Random();
//		double value = random.nextDouble() * 14 + 8;
//		String temp = "获取室内温度：" + value;
//
//		// 2. 准备数据，指定字符串的字符集为 utf-8
//		byte[] bytes = temp.getBytes(StandardCharsets.UTF_8);
//
//		// 3. 填充数据到 ByteBuf
//		buffer.writeBytes(bytes);
//
//		return buffer;
//	}
}
