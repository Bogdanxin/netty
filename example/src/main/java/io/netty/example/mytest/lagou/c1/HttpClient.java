package io.netty.example.mytest.lagou.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * @author weixingong.gwx
 * @date 2021/11/16
 */
public class HttpClient {

	public void connect(String host, int port) {

		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap boot = new Bootstrap();
			boot.group(group)
					.channel(NioSocketChannel.class)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new HttpResponseDecoder())
									.addLast(new HttpRequestEncoder())
									.addLast(new HttpClientHandler());
						}
					});

			ChannelFuture future = boot.connect(host, port).sync();
			URI localhost = new URI("http://127.0.0.0:8088");
			String content = "hello world";

			DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
					localhost.toASCIIString(), Unpooled.wrappedBuffer(content.getBytes(StandardCharsets.UTF_8)));

			request.headers().set(HttpHeaderNames.HOST, host);
			request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
			future.channel().write(request);
			future.channel().flush();
			future.channel().closeFuture().sync();

		} catch (InterruptedException | URISyntaxException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) {

		new HttpClient().connect("127.0.0.1", 8088);
	}
}
