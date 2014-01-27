package com.tommo.concurrent.nonblocking;

import java.util.concurrent.atomic.AtomicReference;

public class Stack {
	AtomicReference<Node> headRef = new AtomicReference<Node>(); // singly ll implementation
	public void push(int number) {
		Node toAdd = new Node(number);
		
		while(true) {
			Node currHead = headRef.get();
			toAdd.next = currHead;
			
			if(headRef.compareAndSet(currHead, toAdd))
				return;
		}
	}
	
	public Integer pop() {
		while(true) {
			Node head = headRef.get();
			
			if(headRef.compareAndSet(head, head.next))
				return head.value;
		}
		
	}
	
	
	private static class Node {
		int value;
		Node next;
		
		Node(int value) {
			this.value = value;
		}
		
		
	}
	
}
