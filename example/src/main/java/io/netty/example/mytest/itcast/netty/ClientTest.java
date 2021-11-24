package io.netty.example.mytest.itcast.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author gwx
 * @date 2021/11/23 10:16 下午
 */
@Slf4j
public class ClientTest {

	public static void main(String[] args) throws InterruptedException {
//		Bootstrap boot = new Bootstrap();
//		NioEventLoopGroup group = new NioEventLoopGroup();
//		Channel channel = boot.group(group)
//				.channel(NioSocketChannel.class)
//				.handler(new ChannelInitializer<NioSocketChannel>() {
//					@Override
//					protected void initChannel(NioSocketChannel ch) throws Exception {
//						ch.pipeline().addLast(new LoggingHandler());
//						ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//							@Override
//							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//								ByteBuf buf = (ByteBuf) msg;
//								log.debug("msg is {}", msg);
//								super.channelRead(ctx, msg);
//							}
//						});
//					}
//				})
//				.connect(new InetSocketAddress("localhost", 8080))
//				.sync().channel();
//
//		new Thread(() -> {
//			Scanner scanner = new Scanner(System.in);
//			while (true) {
//				String line = scanner.nextLine();
//				if ("q".equals(line)) {
//					channel.close();
//					break;
//				}
//				channel.writeAndFlush(line);
//			}
//		}).start();
//
//		ChannelFuture closeFuture = channel.closeFuture();
//		closeFuture.addListener(new ChannelFutureListener() {
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				log.debug("close connect: {}", future.channel());
//				group.shutdownGracefully();
//			}
//		});

		NioEventLoopGroup group = new NioEventLoopGroup();
		Channel channel = new Bootstrap()
				.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new StringEncoder());
						ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) {
								ByteBuf buffer = (ByteBuf) msg;
								System.out.println(buffer.toString(Charset.defaultCharset()));

								// 思考：需要释放 buffer 吗
							}
						});
					}
				}).connect("127.0.0.1", 8080).sync().channel();

		channel.closeFuture().addListener(future -> {
			group.shutdownGracefully();
		});

		new Thread(() -> {
			Scanner scanner = new Scanner(System.in);
			while (true) {
				String line = scanner.nextLine();
				if ("q".equals(line)) {
					channel.close();
					break;
				}
				channel.writeAndFlush(line);
			}
		}).start();
	}
}
