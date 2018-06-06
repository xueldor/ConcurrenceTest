package com.xueldor.concurrence.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {
	public static void main(String[] args) {
		//当有5个线程调用了cBarrier.await()后，MainTask线程会执行
		final CyclicBarrier cBarrier = new CyclicBarrier(5, new MainTask());
		
		final int tCount = 5;
		Thread[] threads = new Thread[tCount];
		
		for (int i = 0; i < tCount; i++) {
			threads[i] = new Thread(((char)('A' + i)) + "") {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + " is on the way");
					try {
						cBarrier.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " start doing");
				}
			};
			threads[i].start();
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getName());
	}
}

class MainTask implements Runnable{

	@Override
	public void run() {
		//amazing,mainTask运行在最后一个调用await的线程上，其它线程等待mainTask结束
		System.out.println("lets do it together" + Thread.currentThread().getName());
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//当mainTask结束，所有调用await的线程同时恢复往下执行
	}
	
}