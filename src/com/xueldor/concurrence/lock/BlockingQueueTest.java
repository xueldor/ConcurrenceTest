package com.xueldor.concurrence.lock;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueTest {
	public static void main(String[] args)  {
		BlockingQueue<String> bQueue = new ArrayBlockingQueue<String>(20);
		Thread putThread = new Thread("Thread-put") {
			@Override
			public void run(){
				for (int i = 0; i < 30; i++) {
					try {
						bQueue.put("element " + i);
						System.out.println("Added element " + i);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		};
		putThread.start();
		
		Thread popThread = new Thread("Thread-pop") {
			@Override
			public void run() {
				while(bQueue.size() < 18) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while(!bQueue.isEmpty()) {
					String ele = bQueue.poll();
					System.out.println("removed " + ele);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		popThread.start();
		
		try {
			putThread.join();
			popThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Program finish,exit");
	}
}
