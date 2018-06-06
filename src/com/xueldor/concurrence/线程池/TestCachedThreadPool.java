package com.xueldor.concurrence.线程池;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class TestCachedThreadPool {
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		Runnable r = new TestRunnable();
		for (int i = 0; i < 5; i++) {
			executorService.execute(r);//复用线程
			System.out.println("********** " + i + " *************");
		}
		executorService.shutdown();//对于CachedThreadPool，如果不手动shutdown，会在60s空闲后自动回收
		executorService.shutdown();//对于CachedThreadPool，如果不手动shutdown，会在60s空闲后自动回收
		executorService.shutdown();//对于CachedThreadPool，如果不手动shutdown，会在60s空闲后自动回收
		executorService.shutdown();//对于CachedThreadPool，如果不手动shutdown，会在60s空闲后自动回收
		System.out.println(executorService.isShutdown());
		System.out.println(executorService.isTerminated());
		System.out.println(((ThreadPoolExecutor)executorService).isTerminating());
		executorService.execute(r);//已经执行了shutdown方法,再提交任务会抛出异常
		
		//test callable
		executorService = Executors.newCachedThreadPool();
		List<Future<String>> futuresList = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Future<String> future = executorService.submit(new TestCallable(i));//会执行callable
			futuresList.add(future);
		}
		executorService.shutdown();//以前提交的任务结束后关闭，不再接收新任务
		for (Future<String> future : futuresList) {
			try {
				//如果Callable没结束，就阻塞直到call()方法完成
				System.out.println(future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}
class TestRunnable implements Runnable{   
    public void run(){
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println(Thread.currentThread().getName() + "线程被调用了。");   
    }   
}

class TestCallable implements Callable<String>{
	private int id;
	
	public TestCallable(int id) {
		this.id = id;
	}

	@Override
	public String call() throws Exception {
		System.out.println("Thread " + Thread.currentThread().getName() + " 调用了call 方法");
		return Thread.currentThread().getName() + " returns " + id;
	}
}