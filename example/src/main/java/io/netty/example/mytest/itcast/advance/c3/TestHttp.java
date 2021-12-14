package io.netty.example.mytest.itcast.advance.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 * @author gwx
 * @date 2021/12/9 7:35 下午
 */
@Slf4j
public class TestHttp {

	public static void main(String[] args) {
		NioEventLoopGroup boss = new NioEventLoopGroup();
		NioEventLoopGroup worker = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			ChannelFuture fu = bootstrap.group(boss, worker)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) throws Exception {
							ch.pipeline().addLast(new LoggingHandler());
							ch.pipeline().addLast(new HttpServerCodec());
							ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
								@Override
								protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {
									// 1. 接收相应
									log.debug("{}", msg.uri());
									// 2. 返回响应，写入数据时候，主要表明数据长度，不然浏览器会一直进行请求
									byte[] bytes = "<h1>hello, world!".getBytes(StandardCharsets.UTF_8);
									DefaultFullHttpResponse response = new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
									response.headers().setInt(CONTENT_LENGTH, bytes.length);
									response.content().writeBytes(bytes);
									// 3. 相应写入 channel
									ctx.writeAndFlush(response);
								}
							});
						}
					})
					.bind(8080).sync();
			fu.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
