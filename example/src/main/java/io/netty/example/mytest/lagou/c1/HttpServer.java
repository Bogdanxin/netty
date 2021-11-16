package io.netty.example.mytest.lagou.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.net.InetSocketAddress;

/**
 * @author weixingong.gwx
 * @date 2021/11/16
 */
public class HttpServer {

	public void start(int port) {
		EventLoopGroup boss = new NioEventLoopGroup();
		NioEventLoopGroup worker = new NioEventLoopGroup();

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline()
									.addLast("codec", new HttpServerCodec())
									.addLast("compressor", new HttpContentCompressor())
									.addLast("aggregator", new HttpObjectAggregator(65536))
									.addLast("handler", new HttpServerHandler());
						}
					})
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture future = b.bind().sync();
			System.out.println("Http server started, Listening on " + port);
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			worker.shutdownGracefully();
			boss.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		new HttpServer().start(8088);
	}
}
