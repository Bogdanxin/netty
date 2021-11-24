package io.netty.example.mytest.itcast.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author gwx
 * @date 2021/11/23 10:16 下午
 */
@Slf4j
public class ServerTest {

	public static void main(String[] args) {
//		ServerBootstrap boot = new ServerBootstrap();
//		NioEventLoopGroup worker = new NioEventLoopGroup();
//		NioEventLoopGroup boss = new NioEventLoopGroup();
//		ChannelFuture future = boot.group(boss, worker)
//				.channel(NioServerSocketChannel.class)
//				.childHandler(new ChannelInitializer<NioSocketChannel>() {
//					@Override
//					protected void initChannel(NioSocketChannel ch) throws Exception {
//						ch.pipeline().addLast(new LoggingHandler());
//						ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//							@Override
//							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//								ByteBuf buf = (ByteBuf) msg;
//								log.debug("msg is {}", msg);
//								ctx.writeAndFlush(msg);
////								super.channelRead(ctx, msg);
//								buf.release();
//							}
//						});
//					}
//				})
//				.bind(8080);
//
//		future.addListener(new ChannelFutureListener() {
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				Channel channel = future.channel();
//				log.debug("{}", channel);
//			}
//		});
//

		new ServerBootstrap()
				.group(new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) {
						ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) {
								ByteBuf buffer = (ByteBuf) msg;
								System.out.println(buffer.toString(Charset.defaultCharset()));

								// 建议使用 ctx.alloc() 创建 ByteBuf
								ByteBuf response = ctx.alloc().buffer();
								response.writeBytes(buffer);
								ctx.writeAndFlush(response);

								// 思考：需要释放 buffer 吗
								// 思考：需要释放 response 吗
							}
						});
					}
				}).bind(8080);
	}
}

