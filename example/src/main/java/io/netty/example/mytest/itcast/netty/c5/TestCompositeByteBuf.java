package io.netty.example.mytest.itcast.netty.c5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

/**
 * @author gwx
 * @date 2021/11/23 10:00 下午
 */
public class TestCompositeByteBuf {

	public static void main(String[] args) {
		ByteBuf buf1 = ByteBufAllocator.DEFAULT.buffer();
		buf1.writeBytes(new byte[] {1, 2, 3, 4, 5});
		ByteBuf buf2 = ByteBufAllocator.DEFAULT.buffer();
		buf2.writeBytes(new byte[] {6, 7, 8, 9, 10});

		CompositeByteBuf byteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
		byteBuf.addComponents(true, buf1, buf2);

		TestSlice.log(byteBuf);

	}
}
