package io.netty.example.mytest.itcast.netty.c2;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @author weixingong.gwx
 * @date 2021/11/21
 */
@Slf4j
public class TestNettyPromise {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		// 1. 准备 EventLoopGroup
		EventLoop eventExecutors = new NioEventLoopGroup().next();

		// 2. 主动创建 promise
		DefaultPromise<Integer> promise = new DefaultPromise<>(eventExecutors);

		new Thread(() -> {
			log.debug("开始计算");
			try {
				Thread.sleep(1000);
				int i = 1 / 0;
				promise.setSuccess(123);
			} catch (Exception e) {
				e.printStackTrace();
				promise.setFailure(e);
			}

		}).start();

		log.debug("等待结果");
		log.debug("结果是 : {}", promise.get());
	}
}
