package com.tommo.concurrent.synchronizer;

import static org.junit.Assert.fail;

import java.util.concurrent.Semaphore;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.Threaded;


@RunWith(Enclosed.class)
public class SemaphoreTestSuite {

	public static class AcquireInterruptlyTest extends MultithreadedTestCase {
		Semaphore s;
		
		@Before
		public void initialize() {
			s = new Semaphore(0);
		}

		@Threaded
		public void thread1() throws InterruptedException {
			try {
	            s.acquire();
	            fail("should throw interrupted exception");
	        } catch(InterruptedException success){ assertTick(1); }
		}

		@Threaded
		public void thread2() throws InterruptedException {
			waitForTick(1); 
			getThreadByName("thread1").interrupt();
		}

		@Test
		public void empty() {}
	}
	
}
