package com.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * 
 * @author Peter
 *
 */
public class Test {

	public static void main(String[] args) {

		//ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 1, TimeUnit.SECONDS,
		//		new ArrayBlockingQueue<Runnable>(3), new ThreadPoolExecutor.DiscardPolicy());

		 ExecutorService threadPool = Executors.newFixedThreadPool(10);

		for (int i = 0; i < 20; i++) {
			System.out.println("数据 " + i);
			threadPool.execute(new ThreadPoolTask(" " + i));

		}
		System.out.println("over-----------------------");
	}
	// ThreadPoolExecutor.AbortPolicy()              当线程池中的数量等于最大线程数时、直接抛出抛出Java.util.concurrent.RejectedExecutionException异常
	// ThreadPoolExecutor.CallerRunsPolicy()       当线程池中的数量等于最大线程数时、重试执行当前的任务，交由调用者线程来执行任务
	// ThreadPoolExecutor.DiscardOldestPolicy()   当线程池中的数量等于最大线程数时、抛弃线程池中最后一个要执行的任务，并执行新传入的任务
	// ThreadPoolExecutor.DiscardPolicy()            当线程池中的数量等于最大线程数时，不做任何动作

}
