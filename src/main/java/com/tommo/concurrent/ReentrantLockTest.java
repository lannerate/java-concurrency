package com.tommo.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantLockTest {

	public static final boolean FAIR = true;
	private static final int NUM_THREADS = 3;

	private static volatile int expectedIndex = 0;

	public static void main(String[] args) throws InterruptedException {
		ReentrantReadWriteLock.WriteLock lock = new ReentrantReadWriteLock(FAIR).writeLock();

		lock.lock();

		for (int i = 0; i < NUM_THREADS; i++) {
			new Thread(new ExampleRunnable(i, lock)).start();

			Thread.sleep(10);
		}

		lock.unlock();
	}

	private static class ExampleRunnable implements Runnable {
		private final int index;
		private final ReentrantReadWriteLock.WriteLock writeLock;

		public ExampleRunnable(int index, ReentrantReadWriteLock.WriteLock writeLock) {
			this.index = index;
			this.writeLock = writeLock;
		}
		
		
		public void run() {
			while(true) {
				writeLock.lock();
				try {
					Thread.sleep(10); // used as a wait period so that the previous thread can obtain the lock without contending with this current thread
				} catch (InterruptedException e) {
				}
				if (index != expectedIndex) {
					System.out.printf("Unexpected thread obtained lock! " +
							"Expected: %d Actual: %d%n", expectedIndex, index);
					System.exit(0);
				}

				expectedIndex = (expectedIndex+1) % NUM_THREADS;
				writeLock.unlock();
			}
		}
	}


}
