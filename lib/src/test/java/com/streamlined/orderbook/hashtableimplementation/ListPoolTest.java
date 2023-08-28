package com.streamlined.orderbook.hashtableimplementation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ListPoolTest {

	@Test
	@DisplayName("pool of capacity 0 should be empty and full")
	void test1() {
		ListPool pool = new ListPool(0, 10);

		assertTrue(pool.isEmpty());
		assertTrue(pool.isFull());
		assertEquals(0, pool.getSize());
	}

	@Test
	@DisplayName("pool of capacity 1 should contain one list and be full")
	void test2() {
		ListPool pool = new ListPool(1, 10);

		assertFalse(pool.isEmpty());
		assertTrue(pool.isFull());
		assertEquals(1, pool.getSize());
	}

	@Test
	@DisplayName("pool of capacity 10 should contain ten lists and be full")
	void test3() {
		ListPool pool = new ListPool(10, 10);

		assertFalse(pool.isEmpty());
		assertTrue(pool.isFull());
		assertEquals(10, pool.getSize());
	}

	@Test
	@DisplayName("pool of capacity 10 should be empty and not full if lists not allocated")
	void test4() {
		ListPool pool = new ListPool(10);

		assertTrue(pool.isEmpty());
		assertFalse(pool.isFull());
		assertEquals(0, pool.getSize());
	}

	@Test
	@DisplayName("should add one list to empty pool of capacity 1 and set size to 1")
	void test5() {
		ListPool pool = new ListPool(1);

		pool.add(new ArrayList(true, 10));

		assertTrue(pool.isFull());
		assertFalse(pool.isEmpty());
		assertEquals(1, pool.getSize());
	}

	@Test
	@DisplayName("should add one list to empty pool of capacity 2 and set size to 1")
	void test6() {
		ListPool pool = new ListPool(2);

		pool.add(new ArrayList(true, 10));

		assertFalse(pool.isFull());
		assertFalse(pool.isEmpty());
		assertEquals(1, pool.getSize());
	}

	@Test
	@DisplayName("should add two lists to pool of capacity 2 and set size to 2")
	void test7() {
		ListPool pool = new ListPool(2);

		pool.add(new ArrayList(true, 10));
		boolean added = pool.add(new ArrayList(true, 10));

		assertTrue(pool.isFull());
		assertFalse(pool.isEmpty());
		assertEquals(2, pool.getSize());
		assertTrue(added);
	}

	@Test
	@DisplayName("should add one list to pool of capacity 1 and fail to add second list")
	void test8() {
		ListPool pool = new ListPool(1);

		pool.add(new ArrayList(true, 10));
		boolean added = pool.add(new ArrayList(true, 10));

		assertTrue(pool.isFull());
		assertFalse(pool.isEmpty());
		assertEquals(1, pool.getSize());
		assertFalse(added);
	}

	@Test
	@DisplayName("should fetch same list from pool which was added before")
	void test9() {
		ListPool pool = new ListPool(1);

		List list = new ArrayList(true, 10);
		boolean added = pool.add(list);

		assertTrue(pool.isFull());
		assertFalse(pool.isEmpty());
		assertEquals(1, pool.getSize());
		assertTrue(added);
		assertSame(list, pool.get());
	}

	@Test
	@DisplayName("should not fetch second list from pool with one element")
	void test10() {
		ListPool pool = new ListPool(1);

		List list = new ArrayList(true, 10);
		pool.add(list);
		pool.get();

		assertFalse(pool.isFull());
		assertTrue(pool.isEmpty());
		assertNull(pool.get());
	}

}
