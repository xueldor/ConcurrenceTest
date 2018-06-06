package com.xueldor.concurrence.lock;

import java.util.concurrent.Semaphore;

/**
 * Semaphore类似于锁的lock与 unlock,
 * 但是可以指定同时获的信号的数量
 * 可以用来做流量控制，类似阀门。
 * @author xuexiangyu
 *
 */
public class SemaphoreTest {
	
	public static void main(String[] args) {
		Driver driver = new Driver();//一个车手
		//Semaphore比较诡异的地方在于，
		//release和acquire不一定成对
		//这里先调了两次release，相当于permits变成5
		//似乎release只是简单加一，acquire只是简单减一,不会判断边界值
		driver.semaphore.release();
		driver.semaphore.release();
		
		for (int i = 0; i < 10; i++) {//10辆车
			new Car(driver,"Car" + i).start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	static class Driver{
		//从技术上讲，限制同时可以有3个线程执行driveCar
		//从功能上说，限制一个司机只能同时开三辆车
		public Semaphore semaphore = new Semaphore(3);
		
		public void driveCar(){
//			if(semaphore.tryAcquire())//不阻塞
			try {
				semaphore.acquire();//阻塞
				semaphore.acquire();//调用两次acquire，相当于semaphore.acquire(2)
				System.out.println("Acquired semaphore,start drive " + Thread.currentThread().getName());
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				System.out.println("release semaphore,end drive "  + Thread.currentThread().getName());
				semaphore.release();
			}
		}
	}
	static class Car extends Thread{
		private Driver driver;

		public Car(Driver driver,String carName) {
			this.driver = driver;
			setName(carName);
		}

		@Override
		public void run() {
			driver.driveCar();
		}
	}
}
