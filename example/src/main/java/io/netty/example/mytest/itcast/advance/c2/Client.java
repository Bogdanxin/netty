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

import java.util.Arrays;

/**
 * @author gwx
 * @date 2021/12/9 11:31 上午
 */
@Slf4j
public class Client {

	public static void main(String[] args) {
		send();
		System.out.println("finish");
	}

	private static void send() {
		NioEventLoopGroup worker = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.channel(NioSocketChannel.class)
					.group(worker)
					.handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
							ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {
									ByteBuf buf = ctx.alloc().buffer();
									char c = '0';
									for (int i = 0; i < 10; i++) {
										byte[] bytes = fill10Bytes(++c, i + 1);
										buf.writeBytes(bytes);
									}
									ctx.writeAndFlush(buf);
								}
							});
						}
					});
			ChannelFuture future = bootstrap.connect("localhost", 8080).sync();
			future.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			worker.shutdownGracefully();
		}
	}

	private static byte[] fill10Bytes(char c, int len) {
		byte[] array = new byte[10];
		for (int i = 0; i < len; i++) {
			array[i] = (byte) c;
		}
		for (int i = len; i < 10; i++) {
			array[i] = '_';
		}
		log.debug("array --> {}", Arrays.toString(array));
		return array;
	}
}
