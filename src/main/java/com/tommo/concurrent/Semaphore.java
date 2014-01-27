package com.tommo.concurrent;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Semaphore {
	
	private static class AQS {
		private final int permits;
		private final Sync sync = new Sync();
		
		public AQS(int permits) {
			this.permits = permits;
		}
		
		
		public void acquire() throws InterruptedException {
			sync.acquireSharedInterruptibly(-1);
		}
		
		public void release() {
			sync.releaseShared(-1);
		}		
		
		private class Sync extends AbstractQueuedSynchronizer {
			
			@Override
			protected int tryAcquireShared(int arg)  {
				while(true) {
					int count = getState();
					
					if(count == permits) {
						return -1;
					}
					else if(compareAndSetState(count, count + 1)) 
						return 1;
				}
			}
			
			@Override
			protected boolean tryReleaseShared(int arg) {
				while(true) {
					int count = getState();
					
					if(count == 0)
						return false;
					else if(compareAndSetState(count, count - 1)) {
						return true;
					}
				}
				
			}
		}
	}
	
	private static class Primitive  {
		private final int permits;
		private int count;
		
		
		public Primitive(int permits) {
			this.permits = permits;
			this.count = 0;
		}
		
		
		public void acquire() throws InterruptedException {
			synchronized(this) {
				while(count >= permits)
					wait();  

				count++;
			}
		}
		
		public void release() { 
			synchronized(this) {
				if(count > 0) { // one in one out and waiting only on one condition
					count--;
					notify();
				}
			}
		}
	}
	
	public static void main(String... args) throws InterruptedException {
//		final Primitive sem = new Primitive(1);
//		
//		TestUtil.createAndRun(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					sem.acquire();
//					System.out.println("acquired");
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				sem.release();
//			}
//		});
//		
//		Thread.sleep(10);
//		
//		TestUtil.createAndRun(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					sem.acquire();
//					System.out.println("acquired");
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				sem.release();
//			}
//		});
		
		final AQS sem = new AQS(1);
		
		TestUtil.createAndRun(new Runnable() {
			@Override
			public void run() {
				try {
					sem.acquire();
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sem.release();
			}
		});
		
		Thread.sleep(10);
		
		TestUtil.createAndRun(new Runnable() {
			@Override
			public void run() {
				try {
					sem.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sem.release();
			}
		});
		
	}
	
}
