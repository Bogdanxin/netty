package io.netty.example.mytest.lagou.c9.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author weixingong.gwx
 * @date 2021/11/18
 */
public class ResponseSampleEncoder extends MessageToByteEncoder<ResponseSample> {
	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseSample msg, ByteBuf out) throws Exception {
		if (msg != null) {
			out.writeBytes(msg.getCode().getBytes());
			out.writeBytes(msg.getData().getBytes());
			out.writeLong(msg.getTimestamp());
		}
	}
}
