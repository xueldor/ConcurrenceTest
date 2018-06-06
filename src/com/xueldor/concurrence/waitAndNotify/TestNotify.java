package com.xueldor.concurrence.waitAndNotify;

public class TestNotify {
	
	private static final Object[] sync = new Object[0];
	
	public static void main(String[] args) {
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (sync) {
					try {
						sync.wait();
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("T1");
				
			}
		});
		t1.start();
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (sync) {
					try {
						sync.wait();
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("T2");
				
			}
		});
		t2.start();
		Thread t3 = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (sync) {
					try {
						sync.wait();
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("T3");
				
			}
		});
		t3.start();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		synchronized (sync) {
//			sync.notifyAll();//全部唤醒，但是只有一个线程能够得到锁

			//由于调用了两次notify，所以会唤醒两个线程,第三个线程会永远wait
			//第二个唤醒的线程会等第一个唤醒的线程释放锁后才往下执行
			sync.notify();
			sync.notify();
		}
		
	}
}
