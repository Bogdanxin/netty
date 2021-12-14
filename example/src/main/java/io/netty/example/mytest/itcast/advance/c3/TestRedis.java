package io.netty.example.mytest.itcast.advance.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author gwx
 * @date 2021/12/9 4:26 下午
 */
@Slf4j
public class TestRedis {

	public static void main(String[] args) {
		final byte[] LINE = {13, 10};
		NioEventLoopGroup worker = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			boot.group(worker)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LoggingHandler());
							ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {
									ByteBuf buff = ctx.alloc().buffer();
									buff.writeBytes("*3".getBytes());
									buff.writeBytes(LINE);
									buff.writeBytes("$3".getBytes());
									buff.writeBytes(LINE);
									buff.writeBytes("set".getBytes());
									buff.writeBytes(LINE);
									buff.writeBytes("$4".getBytes());
									buff.writeBytes(LINE);
									buff.writeBytes("name".getBytes());
									buff.writeBytes(LINE);
									buff.writeBytes("$8".getBytes());
									buff.writeBytes(LINE);
									buff.writeBytes("zhangsan".getBytes());
									buff.writeBytes(LINE);
									ctx.writeAndFlush(buff);
								}

								@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
									ByteBuf buf = (ByteBuf) msg;
									log.debug("{}", buf.toString(StandardCharsets.UTF_8));
								}
							});
						}
					});
			ChannelFuture future = boot.connect("localhost", 6379).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			worker.shutdownGracefully();
		}
	}

//	public static void main(String[] args) {
//		final byte[] LINE = {13, 10};
//		NioEventLoopGroup worker = new NioEventLoopGroup();
//		try {
//			Bootstrap bootstrap = new Bootstrap();
//			bootstrap.channel(NioSocketChannel.class);
//			bootstrap.group(worker);
//			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
//				@Override
//				protected void initChannel(SocketChannel ch) {
//					ch.pipeline().addLast(new LoggingHandler());
//					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//						@Override
//						public void channelActive(ChannelHandlerContext ctx) {
//							ByteBuf buf = ctx.alloc().buffer();
//							buf.writeBytes("*3".getBytes());
//							buf.writeBytes(LINE);
//							buf.writeBytes("$3".getBytes());
//							buf.writeBytes(LINE);
//							buf.writeBytes("set".getBytes());
//							buf.writeBytes(LINE);
//							buf.writeBytes("$4".getBytes());
//							buf.writeBytes(LINE);
//							buf.writeBytes("name".getBytes());
//							buf.writeBytes(LINE);
//							buf.writeBytes("$8".getBytes());
//							buf.writeBytes(LINE);
//							buf.writeBytes("zhangsan".getBytes());
//							buf.writeBytes(LINE);
//							ctx.writeAndFlush(buf);
//						}
//
//						@Override
//						public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//							ByteBuf buf = (ByteBuf) msg;
//							System.out.println(buf.toString(Charset.defaultCharset()));
//						}
//					});
//				}
//			});
//			ChannelFuture channelFuture = bootstrap.connect("localhost", 6379).sync();
//			channelFuture.channel().closeFuture().sync();
//		} catch (InterruptedException e) {
//			log.error("client error", e);
//		} finally {
//			worker.shutdownGracefully();
//		}
//	}
}
