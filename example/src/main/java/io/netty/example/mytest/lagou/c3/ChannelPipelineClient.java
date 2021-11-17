package io.netty.example.mytest.lagou.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author weixingong.gwx
 * @date 2021/11/17
 */
public class ChannelPipelineClient {

	public static void main(String[] args) throws InterruptedException {
		new Bootstrap()
				.group(new NioEventLoopGroup())
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new StringEncoder());
					}
				})
				.connect(new InetSocketAddress("localhost",8080))
				.sync()
				.channel()
				.writeAndFlush("123");
	}
}
