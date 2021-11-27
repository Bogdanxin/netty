package io.netty.example.mytest.itcast.advance.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gwx
 * @date 2021/11/24 9:46 下午
 */
@Slf4j
public class HelloWorldServerTest {

	void start() {
		NioEventLoopGroup boss = new NioEventLoopGroup();
		NioEventLoopGroup worker = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_RCVBUF, 5)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
						}
					});

			ChannelFuture future = bootstrap.bind(8070).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			log.error("server error: {}", e.toString());
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

//	public static void main(String[] args) {
//		new HelloWorldTest().start();
//	}

//	void start() {
//		NioEventLoopGroup boss = new NioEventLoopGroup();
//		NioEventLoopGroup worker = new NioEventLoopGroup();
//		try {
//			ServerBootstrap serverBootstrap = new ServerBootstrap();
//			serverBootstrap.channel(NioServerSocketChannel.class);
//			// 调整系统的接收缓冲器（滑动窗口）
//            serverBootstrap.option(ChannelOption.SO_RCVBUF, 10);
//			// 调整 netty 的接收缓冲区（byteBuf）
////			serverBootstrap.childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(16, 16, 16));
//			serverBootstrap.group(boss, worker);
//			serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
//				@Override
//				protected void initChannel(SocketChannel ch) throws Exception {
//					ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
//				}
//			});
//			ChannelFuture channelFuture = serverBootstrap.bind(8070).sync();
//			channelFuture.channel().closeFuture().sync();
//		} catch (InterruptedException e) {
//			log.error("server error", e);
//		} finally {
//			boss.shutdownGracefully();
//			worker.shutdownGracefully();
//		}
//	}

	public static void main(String[] args) {
		new HelloWorldServerTest().start();
	}
}
