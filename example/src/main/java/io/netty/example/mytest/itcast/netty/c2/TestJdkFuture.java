package io.netty.example.mytest.itcast.netty.c2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 测试 jdk 中的 future 接口
 * @author weixingong.gwx
 * @date 2021/11/21
 */
@Slf4j
public class TestJdkFuture {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		ExecutorService service = Executors.newFixedThreadPool(2);
		Future<Integer> fur = service.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				Thread.sleep(1000);
				return 100;
			}
		});

		log.debug("等待结果...");
		log.debug("结果是 {}" , fur.get());
	}
}
