package com.xueldor.concurrence.lock;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch的作用是
 * 一组线程等待计数器归零
 * 如果调用CountDownLatch#await时已经是0，则不会阻塞
 * CountDownLatch对象已经归零后可以继续调用countDown,但计数永远是0
 * @author xuexiangyu
 */
public class CountDownLatchTest {
	public static void main(String[] args) {
		final CountDownLatch count = new CountDownLatch(2);
		
		for (int i = 0; i < 5; i++) {
			final int c = i;
			new Thread("Thread" + i) {
				@Override
				public void run() {
					long waitTime = 0,startTime;
					try {
						Thread.sleep(1100 * c);
						System.out.println(Thread.currentThread().getName() + " wait");
						waitTime = System.currentTimeMillis();
						count.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					startTime = System.currentTimeMillis();
					System.out.println(Thread.currentThread().getName() + " start," + (startTime - waitTime));
				}
				
			}.start();
		}
		System.out.println("Count " + count.getCount());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		count.countDown();
		System.out.println("Count " + count.getCount());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		count.countDown();//到这里计数器归零，所有await的线程恢复执行
		System.out.println("Count " + count.getCount());//输出0
		count.countDown();//已经归零后再调用countDown还是0
		System.out.println("Count " + count.getCount());//输出0
	}
}
