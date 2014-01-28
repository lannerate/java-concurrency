package com.tommo.concurrent.nonblocking;

import java.util.concurrent.atomic.AtomicReference;

public class Queue {
	private final Node dummy = new Node(-1);

	private final AtomicReference<Node> headref = new AtomicReference<Node>(dummy);
	private final AtomicReference<Node> tailref = new AtomicReference<Node>(dummy);
	
	
	public Integer remove() {
		while (true) {
			Node head = headref.get();
			Node next = head.nextref.get();
			Node tail = tailref.get();
			
			if(head == tail) {  // queue is empty or tail ref is not updated yet
				if(next == null) 
					return null;
				else 
					tailref.compareAndSet(tail, next);  // advance
			} else {
				if(headref.compareAndSet(head, next)) {
					return next.value;
				}
			}
		}
	}

	public void insert(int value) {
		Node newNode = new Node(value);

		while (true) {
			Node tail = tailref.get();

			if (tail.nextref.compareAndSet(null, newNode)) {
				tailref.compareAndSet(tail, newNode); // let other thread do it
														// if not finished
				return;
			} else {
				Node tailNext = tail.nextref.get();

				if (tailNext != null) { // ensure that it is indeed not advanced
										// by some other thread
					tailref.compareAndSet(tail, tailNext);
				}
			}
		}
	}

	private static class Node {
		AtomicReference<Node> nextref;
		int value;

		public Node(int value) {
			this.value = value;
		}
	}
}
