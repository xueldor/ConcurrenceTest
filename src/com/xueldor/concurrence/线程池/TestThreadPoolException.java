package com.xueldor.concurrence.线程池;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestThreadPoolException {
	public static void main(String[] args) {
		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<String> future = service.submit(()->{
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(true) {
				System.err.println("Thread id " + Thread.currentThread().getId());
				throw new RuntimeException("hehehe");
			}
			return "hahaha";
		});
		
		try {
			System.out.println(future.get());
		} catch (InterruptedException | ExecutionException e) {
			//Callable里面抛出的异常会传到这里。
			//通过debug可以看到即使子线程抛出未捕获异常，子线程本身不会结束
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		service.shutdown();//允许执行以前提交的任务
//		service.shutdownNow();//阻止等待任务启动并试图停止当前正在执行的任务
		System.out.println("exit");
	}
}
