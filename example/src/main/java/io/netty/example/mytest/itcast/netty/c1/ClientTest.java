package io.netty.example.mytest.itcast.netty.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author weixingong.gwx
 * @date 2021/11/16
 */
public class ClientTest {

	public static void main(String[] args) throws InterruptedException {

		Channel channel = new Bootstrap()
				// 2. 添加 EventLoop
				.group(new NioEventLoopGroup())
				// 3. 选择客户端 channel 实现ChannelDuplexHandler
				.channel(NioSocketChannel.class)
				// 4. 添加处理器
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override // 在连接建立后被调用
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new StringEncoder());
					}
				})
				// 5. 连接到服务器
				.connect(new InetSocketAddress("localhost", 8080))
				.sync()
				.channel();
		System.out.println("====");
		// 6. 向服务器发送数据
				channel.writeAndFlush("hello, world");

		System.out.println("-----");
		System.out.println();
	}
}
