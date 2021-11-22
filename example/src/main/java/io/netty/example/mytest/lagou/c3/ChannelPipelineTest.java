package io.netty.example.mytest.lagou.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.mytest.lagou.c3.handler.SampleInBoundHandler;
import io.netty.example.mytest.lagou.c3.handler.SampleOutBoundHandler;

/**
 * @author weixingong.gwx
 * @date 2021/11/17
 */
public class ChannelPipelineTest {

	public static void main(String[] args) {
		new ServerBootstrap()
				.group(new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new SampleInBoundHandler("SampleInBoundHandlerA", false))
								.addLast(new SampleInBoundHandler("SampleInBoundHandlerB", false))
								.addLast(new SampleInBoundHandler("SampleInBoundHandlerC", true));
						ch.pipeline().addLast(new SampleOutBoundHandler("SampleOutBoundHandlerA"))
								.addLast(new SampleOutBoundHandler("SampleOutBoundHandlerB"))
								.addLast(new SampleOutBoundHandler("SampleOutBoundHandlerC"));
					}
				})
				.bind(8080);
	}
}
