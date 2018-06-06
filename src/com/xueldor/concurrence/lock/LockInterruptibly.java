package com.xueldor.concurrence.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockInterruptibly {
	private  Lock lock = new ReentrantLock();//非公平锁相当于synchronized
	
	public void write() {
		lock.lock();//lock放到try块外面
		lock.lock();//ReentrantLock,可重入锁
		try {
			long start = System.currentTimeMillis();
			System.out.println("Start to write...");
			while(System.currentTimeMillis() < start + 60000) {//1min
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
			}
		}finally {
			lock.unlock();//unlock必须放到finally里面
			//lock和unlock必须是成对的
			//否则如果lock次数大于unlock,则锁不会释放
			//如果lock次数少于unlock,在unlock时会抛出IllegalMonitorStateException
			lock.unlock();
		}
	}
	public void read() throws InterruptedException{
		lock.lockInterruptibly();//可以响应中断
		try {
			System.out.println("Read now");
		}finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		final LockInterruptibly lockInterruptibly = new LockInterruptibly();
		Thread write = new Thread("Thread-write") {
			@Override
			public void run() {
				lockInterruptibly.write();
				System.err.println("I finish write");
			}
		};
		
		Thread read = new Thread("Thread-read") {
			@Override
			public void run() {
				try {
					lockInterruptibly.read();
					System.out.println("I finish reading");
				} catch (InterruptedException e) {
					System.err.println("I give up reading");
				}
			}
		};
		write.start();
		read.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//5s后read线程仍然无法获取锁，中断等待，让read线程去处理其它事务
		read.interrupt();
		
	}
}









