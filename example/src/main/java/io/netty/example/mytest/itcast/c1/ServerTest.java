package io.netty.example.mytest.itcast.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.mytest.lagou.c3.handler.SampleInBoundHandler;
import io.netty.example.mytest.lagou.c3.handler.SampleOutBoundHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author weixingong.gwx
 * @date 2021/11/16
 */
@Slf4j
public class ServerTest {

	public static void main(String[] args) {
		final EventLoopGroup group = new DefaultEventLoopGroup();
		new ServerBootstrap()
				.group(new NioEventLoopGroup(), new NioEventLoopGroup(2))
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline()
//								.addLast("handler1", new ChannelInboundHandlerAdapter() {
//									@Override
//									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//										String result = ((ByteBuf) msg).toString(Charset.defaultCharset());
//										log.debug(result);
//										ctx.fireChannelRead(msg);
//									}})
//								.addLast(group, "handler2", new ChannelInboundHandlerAdapter() {
//									@Override
//									public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//										String result = ((ByteBuf) msg).toString(Charset.defaultCharset());
//										log.debug(result);
//									}});
								.addLast(new SampleInBoundHandler("test1", true))
						.addLast(new SampleOutBoundHandler("test1"));
					}
				})
				.bind(8000);

		log.debug("===test===");

	}
}
