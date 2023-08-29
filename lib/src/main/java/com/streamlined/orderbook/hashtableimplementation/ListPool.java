package com.streamlined.orderbook.hashtableimplementation;

public class ListPool {

	private List[] pool;
	private int size;

	public ListPool(int capacity) {
		pool = new List[capacity];
	}

	public ListPool(int capacity, int listSize) {
		this(capacity);
		allocate(capacity, listSize);
	}

	private void allocate(int capacity, int listSize) {
		for (int k = 0; k < capacity; k++) {
			add(new OrderedArrayList(true, listSize));
		}
	}

	public boolean isFull() {
		return size == pool.length;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int getSize() {
		return size;
	}

	public boolean add(List list) {
		if (!isFull()) {
			pool[size++] = list;
			return true;
		}
		return false;
	}

	public List get() {
		if (isEmpty()) {
			return null;
		}
		return pool[--size];
	}

}
