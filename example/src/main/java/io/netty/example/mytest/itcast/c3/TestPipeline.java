package io.netty.example.mytest.itcast.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author weixingong.gwx
 * @date 2021/11/21
 */
@Slf4j
public class TestPipeline {
	public static void main(String[] args) {
		new ServerBootstrap()
				.group(new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						// 1. 通过 channel 获取 pipeline
						ChannelPipeline pipeline = ch.pipeline();
						// 2. 添加处理器   head -> addHandler() -> tail
						pipeline.addLast("h1", new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								log.debug("1");
//								// super.channelRead 方法会将当前结果传递给下一个 handler
//								// 如果不调用，入站链会断开
								super.channelRead(ctx, msg);
								ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("hello".getBytes()));
							}
						});
						pipeline.addLast("h4", new ChannelOutboundHandlerAdapter() {
							@Override
							public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
								log.debug("4");
								super.write(ctx, msg, promise);
							}
						});
						pipeline.addLast("h2", new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								log.debug("2");
								super.channelRead(ctx, msg);

							}
						});


						pipeline.addLast("h5", new ChannelOutboundHandlerAdapter() {
							@Override
							public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
								log.debug("5");
								super.write(ctx, msg, promise);
							}
						});
						pipeline.addLast("h3", new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
								log.debug("3");
								super.channelRead(ctx, msg);
//								ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));
								ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("server...".getBytes()));
							}
						});



						pipeline.addLast("h6", new ChannelOutboundHandlerAdapter() {
							@Override
							public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
								log.debug("6");
								super.write(ctx, msg, promise);
							}
						});
					}
				})
				.bind(8080);
	}

}
