package com.xueldor.concurrence.waitAndNotify;

public class TestInterrupted {
	public static void main(String[] args) {
		TestInterrupted testInterrupted = new TestInterrupted();
		Thread.currentThread().interrupt();//只会给线程设置一个为true的中断标志
		
		try {
			synchronized (testInterrupted) {
				//由于前面中断标志设置为true，所以这里立即抛出InterruptedException，并将标志重新设置为false
				testInterrupted.wait();
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
			return;
		}
	}
}
