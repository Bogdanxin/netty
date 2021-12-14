package io.netty.example.mytest.itcast.advance.c5;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gwx
 * @date 2021/12/11 11:25 AM
 */
@Slf4j
public class TestByteBuf {

	public static void main(String[] args) {
		new ServerBootstrap()
				.group(new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.ALLOCATOR, new UnpooledByteBufAllocator(false))
//				.childOption(ChannelOption.RCVBUF_ALLOCATOR, new DefaultMaxBytesRecvByteBufAllocator() )
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LoggingHandler());
						ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//								ByteBuf buf = ctx.alloc().buffer();
//								log.debug("alloc buf : {}", buf);
								log.debug("receive buf {}", msg);
								System.out.println("");
							}
						});
					}
				})
				.bind(8080);
	}
}
