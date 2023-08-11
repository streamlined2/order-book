package com.streamlined.orderbook.hashtableimplementation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinkedListTest {

	@Test
	@DisplayName("newly created list should be empty")
	void test1() {
		LinkedList list = new LinkedList();

		assertEquals(0, list.getSize());
		assertTrue(list.isEmpty());
	}

	@Test
	@DisplayName("list should contain one element passed to constructor")
	void test2() {
		LinkedList list = new LinkedList(new int[] { 1 }, new int[] { 10 });

		assertEquals(1, list.getSize());
		assertFalse(list.isEmpty());

		Node node = list.iterator().next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("should contain one element after adding it to empty list")
	void test3() {
		LinkedList list = new LinkedList();

		list.add(1, 10);

		assertEquals(1, list.getSize());
		assertFalse(list.isEmpty());

		Node node = list.iterator().next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("element should be added before another one")
	void test4() {
		LinkedList list = new LinkedList(new int[] { 2 }, new int[] { 10 });

		list.add(1, 5);

		assertEquals(2, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(1, node.getOrder());
		assertEquals(5, node.getVolume());
		node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("element should be added after another one")
	void test5() {
		LinkedList list = new LinkedList(new int[] { 1 }, new int[] { 5 });

		list.add(2, 10);

		assertEquals(2, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(1, node.getOrder());
		assertEquals(5, node.getVolume());
		node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("element should be added after head but before last one")
	void test6() {
		LinkedList list = new LinkedList(new int[] { 3, 1 }, new int[] { 15, 5 });

		list.add(2, 10);

		assertEquals(3, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(1, node.getOrder());
		assertEquals(5, node.getVolume());
		node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(10, node.getVolume());
		node = i.next();
		assertEquals(3, node.getOrder());
		assertEquals(15, node.getVolume());
	}

	@Test
	@DisplayName("element should be added after last one")
	void test7() {
		LinkedList list = new LinkedList(new int[] { 2, 1 }, new int[] { 10, 5 });

		list.add(3, 15);

		assertEquals(3, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(1, node.getOrder());
		assertEquals(5, node.getVolume());
		node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(10, node.getVolume());
		node = i.next();
		assertEquals(3, node.getOrder());
		assertEquals(15, node.getVolume());
	}

	@Test
	@DisplayName("size should be changed for every element of list")
	void test8() {
		LinkedList list = new LinkedList(new int[] { 1, 2 }, new int[] { 5, 10 });

		list.setVolume(1, 10);
		list.setVolume(2, 20);

		assertEquals(2, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
		node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(20, node.getVolume());
	}

	@Test
	@DisplayName("hasNext should return false for empty list")
	void test9() {
		LinkedList list = new LinkedList();
		Iterator<Node> i = list.iterator();

		assertFalse(i.hasNext());
	}

	@Test
	@DisplayName("next should throw exception if no elements left in list")
	void test10() {
		LinkedList list = new LinkedList();
		Iterator<Node> i = list.iterator();

		assertThrows(NoSuchElementException.class, () -> i.next());
	}

	@Test
	@DisplayName("next should return element from list")
	void test11() {
		LinkedList list = new LinkedList(new int[] { 1 }, new int[] { 10 });
		Iterator<Node> i = list.iterator();

		Node node = i.next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("remove operation for empty list should throw exception")
	void test12() {
		LinkedList list = new LinkedList();

		Iterator<Node> i = list.iterator();
		assertThrows(NoSuchElementException.class, () -> i.remove());
	}

	@Test
	@DisplayName("remove operation should throw exception if no preliminary call of next was done")
	void test13() {
		LinkedList list = new LinkedList(new int[] { 1 }, new int[] { 10 });

		Iterator<Node> i = list.iterator();
		assertThrows(NoSuchElementException.class, () -> i.remove());
	}

	@Test
	@DisplayName("remove operation for one element list should make it empty")
	void test14() {
		LinkedList list = new LinkedList(new int[] { 1 }, new int[] { 10 });

		Iterator<Node> i = list.iterator();
		i.next();
		i.remove();

		assertEquals(0, list.getSize());
		assertTrue(list.isEmpty());
	}

	@Test
	@DisplayName("two consequent remove operations for two element list should make it empty list")
	void test15() {
		LinkedList list = new LinkedList(new int[] { 1, 2 }, new int[] { 10, 20 });

		Iterator<Node> i = list.iterator();
		i.next();
		i.remove();

		assertEquals(1, list.getSize());
		assertFalse(list.isEmpty());

		Node node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(20, node.getVolume());

		i.remove();
		assertEquals(0, list.getSize());
		assertTrue(list.isEmpty());
	}

	@Test
	@DisplayName("remove operation amidst list should contract it")
	void test16() {
		LinkedList list = new LinkedList(new int[] { 1, 2, 3 }, new int[] { 10, 20, 30 });

		Iterator<Node> i = list.iterator();
		i.next();

		Node node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(20, node.getVolume());

		i.remove();
		node = i.next();
		assertEquals(3, node.getOrder());
		assertEquals(30, node.getVolume());

		assertEquals(2, list.getSize());
	}

	@Test
	@DisplayName("remove operation at the tail list should contract it")
	void test17() {
		LinkedList list = new LinkedList(new int[] { 1, 2, 3 }, new int[] { 10, 20, 30 });

		Iterator<Node> i = list.iterator();
		i.next();
		i.next();
		Node node = i.next();
		i.remove();
		assertEquals(3, node.getOrder());
		assertEquals(30, node.getVolume());

		assertEquals(2, list.getSize());
	}

}
