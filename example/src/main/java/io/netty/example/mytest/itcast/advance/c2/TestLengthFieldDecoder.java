package io.netty.example.mytest.itcast.advance.c2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

/**
 * @author gwx
 * @date 2021/12/9 3:54 下午
 */
public class TestLengthFieldDecoder {

	public static void main(String[] args) {
		EmbeddedChannel channel = new EmbeddedChannel(
				new LengthFieldBasedFrameDecoder(1024, 0, 4, 1, 5),
				new LoggingHandler(LogLevel.DEBUG)
		);

		ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
		writeBuf(buf, "hello, world!");
		writeBuf(buf, "hi!");
		channel.writeInbound(buf);
	}

	/**
	 * 固定消息的长度字段长度为 4
	 *
	 * @param buf
	 * @param message
	 */
	private static void writeBuf(ByteBuf buf, String message) {
		byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
		int length = bytes.length;
		buf.writeInt(length);
		buf.writeByte(1);
		buf.writeBytes(bytes);
	}

}
