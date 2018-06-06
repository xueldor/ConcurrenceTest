package com.xueldor.concurrence.线程池;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 * @author xuexiangyu
 */
public class CustomThreadPool {
	public static void main(String[] args) {
		BlockingQueue<Runnable> bQueue = new ArrayBlockingQueue<>(5);
		ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 10, 20, TimeUnit.SECONDS, bQueue);
		
		//newCachedThreadPool
//		ThreadPoolExecutor pool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
//                60L, TimeUnit.SECONDS,
//                new SynchronousQueue<Runnable>());
		
		Runnable r = ()->{
			System.out.println(Thread.currentThread().getName() + "正在执行。。。");   
	        try{   
	            Thread.sleep(1000);   
	        }catch(InterruptedException e){   
	            e.printStackTrace();   
	        }  
		};
		
		pool.execute(r);
		pool.execute(r);
		pool.execute(r);
		pool.execute(r);
		pool.execute(r);
		pool.execute(r);
		pool.execute(r);
		pool.execute(r);
		pool.execute(r);
		pool.execute(r);
		pool.execute(r);
		pool.shutdown();
	}
}
