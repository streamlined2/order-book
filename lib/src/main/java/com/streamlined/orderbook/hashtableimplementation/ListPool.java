package com.streamlined.orderbook.hashtableimplementation;

class ListPool {

	private List[] pool;
	private int size;

	ListPool(int capacity) {
		pool = new List[capacity];
	}

	ListPool(int capacity, int listSize) {
		this(capacity);
		allocate(capacity, listSize);
	}

	private void allocate(int capacity, int listSize) {
		for (int k = 0; k < capacity; k++) {
			add(new ArrayList(true, listSize));
		}
	}

	boolean isFull() {
		return size == pool.length;
	}

	boolean isEmpty() {
		return size == 0;
	}

	int getSize() {
		return size;
	}

	boolean add(List list) {
		if (!isFull()) {
			pool[size++] = list;
			return true;
		}
		return false;
	}

	List get() {
		if (isEmpty()) {
			return null;
		}
		return pool[--size];
	}

}
