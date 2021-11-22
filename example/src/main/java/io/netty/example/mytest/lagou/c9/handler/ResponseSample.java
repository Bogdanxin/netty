package io.netty.example.mytest.lagou.c9.handler;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author weixingong.gwx
 * @date 2021/11/18
 */
@Getter
@Setter
public class ResponseSample {

	private String code;

	private String data;

	private long timestamp;

	public ResponseSample(String code, String data, long timestamp) {
		this.code = code;
		this.data = data;
		this.timestamp = timestamp;
	}
}
