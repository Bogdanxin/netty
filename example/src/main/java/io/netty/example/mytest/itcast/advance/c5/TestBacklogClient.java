package io.netty.example.mytest.itcast.advance.c5;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author gwx
 * @date 2021/12/11 11:28 AM
 */
public class TestBacklogClient {

	public static void main(String[] args) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			boot.group(group)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LoggingHandler());
							ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
								@Override
								public void channelActive(ChannelHandlerContext ctx) throws Exception {
									ctx.writeAndFlush(ctx.alloc().buffer().writeBytes("hello".getBytes(StandardCharsets.UTF_8)));
								}
							});
						}
					});
			ChannelFuture future = boot.connect("localhost", 8080).sync();
			future.channel().closeFuture().sync();
		}  catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
