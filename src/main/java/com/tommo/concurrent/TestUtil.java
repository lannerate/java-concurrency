package com.tommo.concurrent;

public class TestUtil {
	private TestUtil() {}
	
	public static void join(Thread... threads) throws InterruptedException {
		for(Thread thread: threads) {
			thread.join();
		}
	}
	
	
	public static void createAndJoin(Runnable runnable, int numThread) throws InterruptedException {
		join(createAndRun(runnable, numThread));
	}
	
	public static void createAndJoin(final Runnable runnable) throws InterruptedException {
		join(createAndRun(runnable));
	}
	
	
	
	public static Thread[] createAndRun(final Runnable runnable, int numThread) {
		Thread arr[] = new Thread[numThread];
		
		for(int i = 0; i < numThread; i++) {
			arr[i] = createAndRun(runnable);
		}
		
		return arr;
	}
	
	public static Thread createAndRun(final Runnable runnable) {
		Thread t = new Thread() {
			public void run() {
				runnable.run();
			}
		};
		
		t.start();
		return t;
	}
}
