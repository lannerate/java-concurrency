package com.tommo.concurrent.nonblocking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.Threaded;


public class CompareAndSetTest extends MultithreadedTestCase {
    AtomicInteger ai;
    
    @Before
    public void initialize() {
        ai = new AtomicInteger(1);
    }

    @Threaded
    public void compareAndSet() {
        while(!ai.compareAndSet(2, 3)) Thread.yield();
    }

    @Threaded
    public void thread2() {        
        assertTrue(ai.compareAndSet(1, 2));
    }
    
    @Test
    public void testFinalValue() {
    	 assertEquals(ai.get(), 3);  
    }
}
