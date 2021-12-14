package io.netty.example.mytest.itcast.advance.c2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * @author gwx
 * @date 2021/12/9 3:15 下午
 */
@Slf4j
public class Client2 {

	public static void main(String[] args) {
		send();
		System.out.println("finish");
	}

	private static StringBuilder makeString(char c, int len) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < len; i++) {
			builder.append(c);
		}
		builder.append('\n');
		return builder;
	}

	private static void send() {
		NioEventLoopGroup worker = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			boot.group(worker)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
							ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {
									ByteBuf buffer = ctx.alloc().buffer();
									char c = '0';
									Random random = new Random();
									for (int i = 0; i < 10; i++) {

										StringBuilder str = makeString(++c, random.nextInt(256));
										buffer.writeBytes(str.toString().getBytes(StandardCharsets.UTF_8));
									}

									ctx.writeAndFlush(buffer);
								}
							});
						}
					});

			ChannelFuture future = boot.connect("localhost", 8080).sync();
			future.channel().closeFuture().sync();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			worker.shutdownGracefully();
		}
	}
}
