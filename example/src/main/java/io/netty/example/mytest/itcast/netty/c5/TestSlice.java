package io.netty.example.mytest.itcast.netty.c5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @author gwx
 * @date 2021/11/23 9:23 下午
 */
public class TestSlice {

	public static void main(String[] args) {
		ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
		buf.writeBytes(new byte[] {'a', 'b', 'c', 'd', 'e','f', 'g', 'h', 'i', 'j'});
		log(buf);

		ByteBuf buf1 = buf.slice(0, 6);
		ByteBuf buf2 = buf.slice(4, 6);
		log(buf1);
		log(buf2);
		buf1.setByte(5, 't');
		log(buf);
		log(buf1);
		log(buf2);

	}

	public static void log(ByteBuf buffer) {
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
