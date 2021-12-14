package io.netty.example.mytest.itcast.source;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author gwx
 * @date 2021/12/13 2:31 PM
 */
public class TestEventLoop {

	public static void main(String[] args) {
		EventLoop eventLoop = new NioEventLoopGroup().next();

		eventLoop.execute(() -> {
			System.out.println("hello");
		});
	}
}
