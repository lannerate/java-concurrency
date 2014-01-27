package com.tommo.concurrent;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
	public static void main(String... args) throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(2);
		
		TestUtil.createAndRun(new Runnable() {
			@Override
			public void run() {
				latch.countDown();
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				
				latch.countDown();
			}
			
		});
		
		System.out.println("Waiting...");
		latch.await();
		System.out.println("Done");
	}
}
