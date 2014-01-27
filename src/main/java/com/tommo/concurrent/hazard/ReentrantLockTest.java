package com.tommo.concurrent.hazard;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.tommo.concurrent.TestUtil;

public class ReentrantLockTest {
	Lock lock = new ReentrantLock();

	public void mOne() {
		lock.lock();
		throwingMethod();
		mTwo();
	}
	
	public void throwingMethod() {
		throw new RuntimeException();
	}
	
	public void mTwo() {
		lock.unlock();
	}
	
	public static void main(String... args) throws InterruptedException {
		
		final ReentrantLockTest test = new ReentrantLockTest();
		
		TestUtil.createAndJoin(new Runnable() {
			public void run() {
				test.mOne();
			}
			
		}, 2);
	}
	
	
}
