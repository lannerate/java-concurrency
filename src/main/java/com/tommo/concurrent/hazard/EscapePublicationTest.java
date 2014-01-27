package com.tommo.concurrent.hazard;

import com.tommo.concurrent.TestUtil;

public class EscapePublicationTest {

	interface Listener {
		void onEvent();
	}

	private static class ThisEscape {
		private final int unsafe;

		ThisEscape(Source source) {
			source.registerListener(new Listener() {
				public void onEvent() {
					doSomething();
				}
			});

			this.unsafe = 42;
		}

		private void doSomething() {
			if (unsafe != 42) {
				System.out.println("Race condition:" + unsafe);
			}
		}
	}


	public static class Source implements Runnable {
		private volatile Listener listener;

		public void run() {
			while(listener == null) ; 
			listener.onEvent(); 
		}

		public void registerListener(Listener eventListener) {
			listener = eventListener;
		}
	}

	public static void main(String... args) throws InterruptedException {
		while(true) {
			Source source = new Source();
			TestUtil.createAndRun(source);
			new ThisEscape(source);
		}
	}

}
