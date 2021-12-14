package io.netty.example.mytest.itcast.netty.c2;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author weixingong.gwx
 * @date 2021/11/21
 */
@Slf4j
public class TestNettyFuture  {
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		NioEventLoopGroup group = new NioEventLoopGroup();
		EventLoop eventLoop = group.next();
		Future<Integer> future = eventLoop.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				log.debug("执行计算");
				Thread.sleep(1000);
				return 100;
			}
		});

		log.debug("等待结果");
		log.debug("结果为 {}", future.get());
//		future.addListener(new GenericFutureListener<Future<? super Integer>>() {
//			@Override
//			public void operationComplete(Future<? super Integer> future) throws Exception {
//				log.debug("接收结果:{}", future.getNow());
//			}
//		})
	}


}