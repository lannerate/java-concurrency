package com.tommo.concurrent.queue;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.Threaded;

@RunWith(Enclosed.class)
public class BlockingQueueTestSuite {
	
	public static class BlockIfQueueFullTest extends MultithreadedTestCase {
		ArrayBlockingQueue<Integer> buf;
		
		@Before
		public void initialize() {
			buf = new ArrayBlockingQueue<Integer>(1);
		}

		@Threaded
		public void thread1() throws InterruptedException {
			buf.put(42);
			buf.put(17);
			assertTick(1);
		}

		@Threaded
		public void thread2() throws InterruptedException {
			waitForTick(1);
			assertThat(Integer.valueOf(42), is(buf.take()));
			assertThat(Integer.valueOf(17), is(buf.take()));
		}

		@Test
		public void emptyUponCompletion() {
			assertThat(buf.isEmpty(), is(true));
		}
	}
}
