package com.xueldor.concurrence.线程池;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolTest {
	public static void main(String[] args) {
		ScheduledThreadPoolTest sTest = new ScheduledThreadPoolTest();
		sTest.test1();
//		sTest.test2();
//		sTest.test3();
//		sTest.test4();
//		sTest.test5();
	}
	
	//scheduleWithFixedDelay 前一个任务完成后延迟固定时间执行第二个
	void test1() {
		long start = System.currentTimeMillis();
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(0);
		ses.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				//每两次打印间隔6秒，本身4s,延迟了2s
				System.out.println(Thread.currentThread().getName() + " start print " + (System.currentTimeMillis()-start));
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(Thread.currentThread().getName() + " end print " + (System.currentTimeMillis()-start));
			}
		}, 2000, 2000, TimeUnit.MILLISECONDS);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("shutdown");
		ses.shutdown();//如果run正在执行，等执行结束后再结束线程池，但该方法本身不会阻塞
	}
	//scheduleAtFixedRate 固定频率执行任务
	void test2() {
		long start = System.currentTimeMillis();
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(0);
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				//每两次打印间隔3秒
				System.out.println(Thread.currentThread().getName() + " print " + (System.currentTimeMillis()-start));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}, 2000, 3000, TimeUnit.MILLISECONDS);
	}
	//scheduleAtFixedRate 固定频率执行任务
	//可以看出固定频率的前提是任务本身的运行时间小于间隔时间
	void test3() {
		long start = System.currentTimeMillis();
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(4);
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				//每两次打印间隔4秒
				//虽然设置了每2秒执行，但是run方法执行需要4s，所以下一次执行只能晚点
				//尽管corePoolSize>1时，run每一次执行在哪个线程上面是不确定的，但是依然会晚点
				System.out.println(Thread.currentThread().getName() + " print " + (System.currentTimeMillis()-start));
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 1000, 2000, TimeUnit.MILLISECONDS);
	}
	//scheduleAtFixedRate 固定频率执行任务
	void test4() {
		long start = System.currentTimeMillis();
		//参数先后设置为1和2，观察打印结果
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " print " + (System.currentTimeMillis()-start));
			}
		};
		
		//启动两个定时任务
		//如果corePoolSize设置为0或1，两个任务只能在一个线程上面执行，因此不能保证相同任务前后执行间隔2s
		//比如，本该执行第一个定时任务，但是第二个正在执行，线程池中又没有闲置线程，
		//这时第一个定时任务要等第二个定时任务的本次执行结束
		
		//如果corePoolSize>1,线程池有空余线程，可以保证两个定时任务都按时执行
		//但是每个任务的每次执行运行在哪个线程上都是不确定的。任务跟线程不应该有关联
		ses.scheduleAtFixedRate(r, 500, 2000, TimeUnit.MILLISECONDS);
		ses.scheduleAtFixedRate(r, 0, 2000, TimeUnit.MILLISECONDS);
	}
	//任务中抛出异常
	void test5() {
		long start = System.currentTimeMillis();
		//ScheduledThreadPool比Timer优越的地方之一:
		//如果TimerTask抛出RuntimeException，Timer线程会挂，会停止所有任务的运行
		//但是ScheduledExecutorService可以保证，task1出现异常时,task1所在线程不会挂,
		//不影响task2的运行,仅task1后续不再周期性执行
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " print1 " + (System.currentTimeMillis()-start));
				throw new RuntimeException("Exception in Task");
			}
		};
		
		ses.scheduleAtFixedRate(r, 500, 2000, TimeUnit.MILLISECONDS);
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + " print2 " + (System.currentTimeMillis()-start));
			}
		}, 0, 2000, TimeUnit.MILLISECONDS);
	}
}
