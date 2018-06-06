package com.xueldor.concurrence.线程池;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {
	public static void main(String[] args) {
		FutureTaskTest ff = new FutureTaskTest();
//		ff.test1();
//		ff.test2();
		System.out.println("main thread " + Thread.currentThread().getName());
		ff.test3();
	}
	
	//通过线程池
	public void test1() {
		FutureTask<String> fTask = new FutureTask<>(new MyCallable());
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(fTask);
		
		try {
			String result = fTask.get();
			System.out.println("execute " + result);//hehehehe
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		Future<?> future = executorService.submit(fTask);//参数fTask是Runnable对象，因此future.get返回null
		try {
			String result = fTask.get();
			System.out.println("submit " + result);//hehehehe
			
			String result2 = (String)future.get();
			System.out.println("submit " + result2);//null
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		executorService.shutdown();
	}
	//通过new Thread
	public void test2() {
		FutureTask<String> fTask = new FutureTask<>(new MyCallable());
		Thread thread = new Thread(fTask);
		thread.start();
		
		try {
			String result = fTask.get();
			System.out.println("thread.start " + result);//hehehehe
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	//回调
	public void test3() {
		MyFutureTask fTask = new MyFutureTask(new MyCallable());
		Thread thread = new Thread(fTask);
		thread.start();
		
		try {
			String result = fTask.get();
			System.out.println("result " + result);//hehehehe
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
class MyCallable implements Callable<String>{

	@Override
	public String call() throws Exception {
		Thread.sleep(3000);
		System.out.println("SubThread name " + Thread.currentThread().getName());
		return "hehehehe";
	}
}

class MyFutureTask extends FutureTask<String>{

	public MyFutureTask(Callable<String> callable) {
		super(callable);
	}

	@Override
	protected void done() {
		System.out.println("done is called on " + Thread.currentThread().getName());
		try {
			System.out.println("Complete result is " + get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
}