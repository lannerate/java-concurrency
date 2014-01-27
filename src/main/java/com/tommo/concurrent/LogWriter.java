package com.tommo.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class LogWriter {
	private final BlockingQueue<String> q;
	private final LoggerThread logger;
	final AtomicLong count = new AtomicLong(0);

	volatile boolean isShutdown = false;

	public LogWriter(BlockingQueue<String> q) {
		logger = new LoggerThread();
		this.q = q;
	}

	public void start() {
		logger.start();
	}

	public long shutdown() throws InterruptedException {
		synchronized (this) {
			isShutdown = true;
		}
		
		logger.interrupt();
		logger.join();
		
		return count.get();
	}

	public void log(String msg) throws InterruptedException {
		synchronized (this) {
			if (!isShutdown) {
				q.put(msg);
			} else
				throw new IllegalStateException("Shutting down");
		}
	}

	private class LoggerThread extends Thread {
		public void run() {
			while (true) {
				try {
					if (isShutdown) {
						
						while (!q.isEmpty()) 
							takeQueue();
						
						break;
					} else {
						takeQueue();
					}
				} catch (Throwable e) {
					e.printStackTrace();
				} 

			}
		}
		
		private void takeQueue() throws InterruptedException {
			q.take();
			count.incrementAndGet();
		}
		
	}
	
	
	public static long spawnLoggingThread() throws InterruptedException {
		final LogWriter writer = new LogWriter(new LinkedBlockingQueue<String>(2));
		
		writer.start();
		
		final AtomicBoolean stop = new AtomicBoolean(false);
		
		TestUtil.createAndRun(new Runnable() {
			public void run() {
				try {
					while(!stop.get()) 				
						writer.log("Something");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 100);
		
		Thread.sleep(1000 * 5);

		stop.set(true);
		
		return writer.shutdown();
	}
	
	
	public static void main(String[] args) throws Exception {
		//		-XX:-UseCounterDecay -XX:CompileThreshold=1
		
		long count = 0;
		
		for(int i = 0; i < 10; i++) {
			long k = spawnLoggingThread();
			System.out.println(k);
			count += k;
		}
		
		System.out.println(count / 10.0);

	}
	
}
