package io.netty.example.mytest.itcast.advance.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author gwx
 * @date 2021/11/24 9:50 下午
 */
@Slf4j
public class HelloWorldClientTest {

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			send();
		}

		System.out.println("finish");
	}

	private static void send() {
		NioEventLoopGroup worker = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(worker)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
								// 会在连接 channel 建立后，触发 active 事件。调用 channelActive 方法
								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {

									ByteBuf buf = ctx.alloc().buffer(16);
									buf.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20});
									ctx.writeAndFlush(buf);
									ctx.channel().close();
								}
							});
						}
					});

			ChannelFuture future = bootstrap.connect(new InetSocketAddress("localhost", 8070)).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			log.debug("client error : {}", e.toString());
		} finally {
			worker.shutdownGracefully();
		}
	}

//	public static void main(String[] args) {
//		for (int i = 0; i < 10; i++) {
//			send();
//		}
//		System.out.println("finish");
//	}
//
//	private static void send() {
//		NioEventLoopGroup worker = new NioEventLoopGroup();
//		try {
//			Bootstrap bootstrap = new Bootstrap();
//			bootstrap.channel(NioSocketChannel.class);
//			bootstrap.group(worker);
//			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
//				@Override
//				protected void initChannel(SocketChannel ch) {
//					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//						// 会在连接 channel 建立成功后，会触发 active 事件
//						@Override
//						public void channelActive(ChannelHandlerContext ctx) {
//							ByteBuf buf = ctx.alloc().buffer(16);
//							buf.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17});
//							ctx.writeAndFlush(buf);
//							ctx.channel().close();
//						}
//					});
//				}
//			});
//			ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8070).sync();
//			channelFuture.channel().closeFuture().sync();
//		} catch (InterruptedException e) {
//			log.error("client error", e);
//		} finally {
//			worker.shutdownGracefully();
//		}
//	}
}
