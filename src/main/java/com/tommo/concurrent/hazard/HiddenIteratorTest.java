package com.tommo.concurrent.hazard;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.tommo.concurrent.TestUtil;

public class HiddenIteratorTest {
	private final Set<Integer> set = new HashSet<Integer>();
	
	synchronized void add(Integer i) { set.add(i); }
	
	synchronized void remove(Integer i) { set.remove(i); }
	
	void addTenThings() throws InterruptedException {
		
		Random r = new Random();
		
		for(int i = 0; i < 10; i++)  
			add(r.nextInt());

		set.toString();
	}

	
	public static void main(String... args) throws InterruptedException {
		final HiddenIteratorTest hidden = new HiddenIteratorTest();
		
		
		while(true) {
			TestUtil.createAndJoin(new Runnable() {
				@Override
				public void run() {
					try {
						hidden.addTenThings();
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}, 2);
		}
	}
}