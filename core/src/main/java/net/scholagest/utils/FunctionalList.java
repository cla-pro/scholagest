package net.scholagest.utils;

public class FunctionalList<T> {
	private T head = null;
	private FunctionalList<T> tail = null;
	
	public FunctionalList(T head) {
		this(head, null);
	}
	
	public FunctionalList(T head, FunctionalList<T> tail) {
		this.head = head;
		this.tail = tail;
	}
	
	public T getHead() {
		return this.head;
	}
	
	public FunctionalList<T> getTail() {
		return this.tail;
	}
}
