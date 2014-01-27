package com.tommo.concurrent.hazard;

import com.tommo.concurrent.TestUtil;

public class UnsafeIntTest {
	static volatile int unsafeInt = 0;


	public static void incrementBy50() {
		for(int i = 0; i < 50; i++) {
			unsafeInt++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String... args) throws InterruptedException {
		while(true) {
			TestUtil.join(TestUtil.createAndRun(new Runnable() {
				@Override
				public void run() {
					incrementBy50();
				}
			}, 2));
			
			System.out.println(unsafeInt);
			unsafeInt = 0;
		}
	}
}
