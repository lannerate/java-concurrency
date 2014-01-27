package com.tommo.concurrent.hazard;

import com.tommo.concurrent.TestUtil;

public class AlienMethod {


	private static class AlienClass2 {

		public synchronized void dangerous(AlienClass1 ac1) throws InterruptedException {
			System.out.println(Thread.currentThread().getName() + " AlienClass2" );
			Thread.sleep((long) (Math.random() * 1000));
			ac1.dangerous(this);
		}
	}


	private static class AlienClass1 {

		public synchronized void dangerous(AlienClass2 ac2) throws InterruptedException {
			System.out.println(Thread.currentThread().getName() + " AlienClass1");
			Thread.sleep((long) (Math.random() * 1000));
			ac2.dangerous(this);
		}

	}

	public static void main(String... args) throws Exception {

		final AlienClass1 ac1 = new AlienClass1();
		final AlienClass2 ac2 = new AlienClass2();


		Thread t1 = TestUtil.createAndRun(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						ac2.dangerous(ac1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		});

		Thread t2 = TestUtil.createAndRun(new Runnable() {
			@Override
			public void run() {

				while(true) {

					try {
						ac1.dangerous(ac2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		t1.join();
		t2.join();
	}
}
