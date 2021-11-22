package io.netty.example.mytest.lagou.c9;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.example.mytest.lagou.c9.handler.RequestSampleHandler;
import io.netty.example.mytest.lagou.c9.handler.ResponseSampleEncoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * @author weixingong.gwx
 * @date 2021/11/18
 */
public class EchoServer {

	public static void main(String[] args) throws InterruptedException {
		new EchoServer().startEchoServer(8088);
	}

	public void startEchoServer(int port) throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();

		try {

			ServerBootstrap boot = new ServerBootstrap();
			boot.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
							ch.pipeline().addLast(new ResponseSampleEncoder());
							ch.pipeline().addLast(new RequestSampleHandler());
						}
					});

			ChannelFuture future = boot.bind(port).sync();
			future.channel().closeFuture().sync();
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

}
