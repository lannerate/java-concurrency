package com.tommo.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {

	public static void main(String... args) throws Exception {

		System.out.println(Thread.currentThread().getName());
		
		final CyclicBarrier barrier = new CyclicBarrier(1, new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + "after!");
			}
		});
		
		
		TestUtil.createAndJoin(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("inside");
					barrier.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (BrokenBarrierException e) {
					e.printStackTrace();
				}
			}
		});
		
		barrier.await();
		
		while(true) {
			
			Object o = new Object();
			if(Object.class.hashCode() == o.hashCode()) {
				System.out.println("He you");
			}
			
		}
		
	}
	
	
}
