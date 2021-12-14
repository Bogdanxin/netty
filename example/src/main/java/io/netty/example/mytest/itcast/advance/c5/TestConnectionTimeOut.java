package io.netty.example.mytest.itcast.advance.c5;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gwx
 * @date 2021/12/11 9:56 AM
 */
@Slf4j
public class TestConnectionTimeOut {

	public static void main(String[] args) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap boot = new Bootstrap();
			ChannelFuture future = boot.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
					.handler(new LoggingHandler())
					.connect("localhost", 8080);
			future.sync().channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("timeout");
		} finally {
			group.shutdownGracefully();
		}
	}
}
