package com.tommo.concurrent;

public class ShutdownHook {
	public static void main(String... args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				while(true) {
					System.out.println("hi");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		System.exit(0);
		
	}
}
