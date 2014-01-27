package com.tommo.concurrent.nonblocking;

import java.util.concurrent.atomic.AtomicInteger;

import com.tommo.concurrent.TestUtil;

public class Counter {
	final AtomicInteger counter = new AtomicInteger(0);

	public int getValue() {
		return counter.get();
	}

	public int increment() {
		while (true) {
			int currentCount = counter.get();

			if (counter.compareAndSet(currentCount, currentCount + 1))
				return currentCount + 1;
		}
	}

	public static void main(String... args) throws InterruptedException {
		final Counter counter = new Counter();

		TestUtil.createAndJoin(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= 100; i++) {
					try {
						Thread.sleep((long) (Math.random() * 50));
						counter.increment();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}, 4);

		System.out.println(counter.getValue());

	}

}
