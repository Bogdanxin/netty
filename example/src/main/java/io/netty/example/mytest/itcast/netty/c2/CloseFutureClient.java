package io.netty.example.mytest.itcast.netty.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author weixingong.gwx
 * @date 2021/11/21
 */

@Slf4j
public class CloseFutureClient {


	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		ChannelFuture future = new Bootstrap()
				.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
						ch.pipeline().addLast(new StringEncoder());
					}
				})
				.connect(new InetSocketAddress("localhost", 8080));

		Channel channel = future.sync().channel();
		log.debug("{}",channel);

		new Thread(() -> {
			Scanner scanner = new Scanner(System.in);
			while (true) {
				String s = scanner.nextLine();
				if ("q".equals(s)) {
					// close 方法是异步操作，当前线程会创建一个线程去关闭当前 channel ，
					// 所以不能够在调用 close 之后立刻进行关闭连接的处理
					channel.close();
					break;
				}
				channel.writeAndFlush(s);
			}
		}, "input").start();

		// 1. 调用 channel 的 closeFuture 方法，返回一个  future 对象，
		// 调用 sync 方法，【main】线程同步等待 channel 的关闭
		ChannelFuture closeFuture = channel.closeFuture();
//		log.debug("waiting close...");
//		closeFuture.sync();
//		log.debug("关闭之后的处理操作");

		// 2. 添加一个 listener
		closeFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				log.debug("close 之后的操作。。。");
				group.shutdownGracefully();
			}
		});
	}
}
