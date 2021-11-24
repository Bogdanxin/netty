package io.netty.example.mytest.itcast.netty.c4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @author weixingong.gwx
 * @date 2021/11/21
 */
public class TestByteBuf {


	public static void main(String[] args) {
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
		log(buffer);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 300; i++) {
			builder.append("a");
		}

		buffer.writeBytes(builder.toString().getBytes());
		log(buffer);
	}

	private static void log(ByteBuf buffer) {
		int length = buffer.readableBytes();
		int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
		StringBuilder buf = new StringBuilder(rows * 80 * 2)
				.append("read index:").append(buffer.readerIndex())
				.append(" write index:").append(buffer.writerIndex())
				.append(" capacity:").append(buffer.capacity())
				.append(NEWLINE);
		appendPrettyHexDump(buf, buffer);
		System.out.println(buf.toString());
	}
}
