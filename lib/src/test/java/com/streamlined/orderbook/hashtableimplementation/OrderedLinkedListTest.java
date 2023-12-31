package com.streamlined.orderbook.hashtableimplementation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.streamlined.orderbook.PriceVolume;
import com.streamlined.orderbook.hashtableimplementation.OrderedLinkedList.Node;

class OrderedLinkedListTest {

	@Test
	@DisplayName("newly created list should be empty")
	void test1() {
		OrderedLinkedList list = new OrderedLinkedList(false);

		assertEquals(0, list.getSize());
		assertTrue(list.isEmpty());
	}

	@Test
	@DisplayName("list should contain one element passed to constructor")
	void test2() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 1 }, new int[] { 10 });

		assertEquals(1, list.getSize());
		assertFalse(list.isEmpty());

		Node node = list.iterator().next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("should contain one element after adding it to empty list")
	void test3() {
		OrderedLinkedList list = new OrderedLinkedList(false);

		list.setAdd(1, 10);

		assertEquals(1, list.getSize());
		assertFalse(list.isEmpty());

		Node node = list.iterator().next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("element should be added before another one")
	void test4() {
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 2 }, new int[] { 10 });

		list.setAdd(1, 5);

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
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 1 }, new int[] { 5 });

		list.setAdd(2, 10);

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
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 3, 1 }, new int[] { 15, 5 });

		list.setAdd(2, 10);

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
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 2, 1 }, new int[] { 10, 5 });

		list.setAdd(3, 15);

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
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 1, 2 }, new int[] { 5, 10 });

		assertFalse(list.setVolume(0, 0));
		assertTrue(list.setVolume(1, 10));
		assertTrue(list.setVolume(2, 20));
		assertFalse(list.setVolume(3, 30));

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
		OrderedLinkedList list = new OrderedLinkedList(false);
		Iterator<Node> i = list.iterator();

		assertFalse(i.hasNext());
	}

	@Test
	@DisplayName("next should throw exception if no elements left in list")
	void test10() {
		OrderedLinkedList list = new OrderedLinkedList(false);
		Iterator<Node> i = list.iterator();

		assertThrows(NoSuchElementException.class, () -> i.next());
	}

	@Test
	@DisplayName("next should return element from list")
	void test11() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 1 }, new int[] { 10 });
		Iterator<Node> i = list.iterator();

		Node node = i.next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("remove operation for empty list should throw exception")
	void test12() {
		OrderedLinkedList list = new OrderedLinkedList(false);

		Iterator<Node> i = list.iterator();
		assertThrows(NoSuchElementException.class, () -> i.remove());
	}

	@Test
	@DisplayName("remove operation should throw exception if no preliminary call of next was done")
	void test13() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 1 }, new int[] { 10 });

		Iterator<Node> i = list.iterator();
		assertThrows(NoSuchElementException.class, () -> i.remove());
	}

	@Test
	@DisplayName("remove operation for one element list should make it empty")
	void test14() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 1 }, new int[] { 10 });

		Iterator<Node> i = list.iterator();
		i.next();
		i.remove();

		assertEquals(0, list.getSize());
		assertTrue(list.isEmpty());
	}

	@Test
	@DisplayName("two consequent remove operations for two element list should make it empty list")
	void test15() {
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 1, 2 }, new int[] { 10, 20 });

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
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 1, 2, 3 }, new int[] { 10, 20, 30 });

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
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 1, 2, 3 }, new int[] { 10, 20, 30 });

		Iterator<Node> i = list.iterator();
		i.next();
		i.next();
		Node node = i.next();
		i.remove();
		assertEquals(3, node.getOrder());
		assertEquals(30, node.getVolume());

		assertEquals(2, list.getSize());
	}

	@Test
	@DisplayName("search operation for empty list should not yield results")
	void test18() {
		OrderedLinkedList list = new OrderedLinkedList(false);

		assertNull(list.getPriceVolumeByPrice(1));
		assertNull(list.getPriceVolumeByPrice(2));
		assertNull(list.getPriceVolumeByPrice(3));
	}

	@Test
	@DisplayName("search operation for non-empty list should not yield results if order is not present in list")
	void test19() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertNull(list.getPriceVolumeByPrice(5));
		assertNull(list.getPriceVolumeByPrice(15));
		assertNull(list.getPriceVolumeByPrice(25));
		assertNull(list.getPriceVolumeByPrice(35));
	}

	@Test
	@DisplayName("search operation for non-empty list should not yield results if order is present in list")
	void test20() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		PriceVolume priceVolume = list.getPriceVolumeByPrice(10);
		assertEquals(10, priceVolume.getPrice());
		priceVolume = list.getPriceVolumeByPrice(20);
		assertEquals(20, priceVolume.getPrice());
		priceVolume = list.getPriceVolumeByPrice(30);
		assertEquals(30, priceVolume.getPrice());
	}

	@Test
	@DisplayName("remove operation for empty list should not yiled results")
	void test21() {
		OrderedLinkedList list = new OrderedLinkedList(false);

		assertFalse(list.remove(0));
		assertFalse(list.remove(1));
		assertFalse(list.remove(2));
	}

	@Test
	@DisplayName("remove operation for non-empty list should remove element")
	void test22() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertTrue(list.remove(10));
		assertEquals(2, list.getSize());
		assertTrue(list.remove(20));
		assertEquals(1, list.getSize());
		assertTrue(list.remove(30));
		assertEquals(0, list.getSize());
		assertTrue(list.isEmpty());
	}

	@Test
	@DisplayName("remove operation for non-empty list should remove found element or return false if not found")
	void test23() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertFalse(list.remove(5));
		assertTrue(list.remove(10));
		assertEquals(2, list.getSize());
		assertFalse(list.remove(15));
		assertTrue(list.remove(20));
		assertEquals(1, list.getSize());
		assertFalse(list.remove(25));
		assertTrue(list.remove(30));
		assertEquals(0, list.getSize());
		assertTrue(list.isEmpty());
	}

	@Test
	@DisplayName("there should be no non-empty node for empty list")
	void test24() {
		OrderedLinkedList list = new OrderedLinkedList(false);

		assertNull(list.getFirstNonEmptyNode());
	}

	@Test
	@DisplayName("there should be found non-empty node for non-empty list")
	void test25() {
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 1, 5, 10, 20, 30 },
				new int[] { 0, 0, 10, 20, 30 });

		PriceVolume priceVolume = list.getFirstNonEmptyNode();
		assertEquals(10, priceVolume.getPrice());
		assertEquals(10, priceVolume.getVolume());
	}

	@Test
	@DisplayName("there should be found non-empty node for non-empty list")
	void test26() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 1, 5, 10, 20, 30, 40, 50 },
				new int[] { 0, 0, 10, 20, 30, 0, 0 });

		PriceVolume priceVolume = list.getFirstNonEmptyNode();
		assertEquals(30, priceVolume.getPrice());
		assertEquals(30, priceVolume.getVolume());
	}

	@Test
	@DisplayName("should be no subtracted volume for empty list")
	void test27() {
		OrderedLinkedList list = new OrderedLinkedList(false);

		assertEquals(0, list.subtractVolume(100).subtractedVolume);
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty ascending list")
	void test28() {
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(5, list.subtractVolume(5).subtractedVolume);
		assertEquals(5, list.getPriceVolumeByPrice(10).getVolume());
		assertEquals(20, list.getPriceVolumeByPrice(20).getVolume());
		assertEquals(30, list.getPriceVolumeByPrice(30).getVolume());
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty ascending list")
	void test29() {
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(15, list.subtractVolume(15).subtractedVolume);
		assertNull(list.getPriceVolumeByPrice(10));
		assertEquals(15, list.getPriceVolumeByPrice(20).getVolume());
		assertEquals(30, list.getPriceVolumeByPrice(30).getVolume());
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty ascending list")
	void test30() {
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(35, list.subtractVolume(35).subtractedVolume);
		assertNull(list.getPriceVolumeByPrice(10));
		assertNull(list.getPriceVolumeByPrice(20));
		assertEquals(25, list.getPriceVolumeByPrice(30).getVolume());
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty ascending list")
	void test31() {
		OrderedLinkedList list = new OrderedLinkedList(true, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(60, list.subtractVolume(65).subtractedVolume);
		assertNull(list.getPriceVolumeByPrice(10));
		assertNull(list.getPriceVolumeByPrice(20));
		assertNull(list.getPriceVolumeByPrice(30));
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty descending list")
	void test32() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(5, list.subtractVolume(5).subtractedVolume);
		assertEquals(10, list.getPriceVolumeByPrice(10).getVolume());
		assertEquals(20, list.getPriceVolumeByPrice(20).getVolume());
		assertEquals(25, list.getPriceVolumeByPrice(30).getVolume());
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty descending list")
	void test33() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(35, list.subtractVolume(35).subtractedVolume);
		assertEquals(10, list.getPriceVolumeByPrice(10).getVolume());
		assertEquals(15, list.getPriceVolumeByPrice(20).getVolume());
		assertNull(list.getPriceVolumeByPrice(30));
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty descending list")
	void test34() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(55, list.subtractVolume(55).subtractedVolume);
		assertEquals(5, list.getPriceVolumeByPrice(10).getVolume());
		assertNull(list.getPriceVolumeByPrice(20));
		assertNull(list.getPriceVolumeByPrice(30));
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty descending list")
	void test35() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(60, list.subtractVolume(65).subtractedVolume);
		assertNull(list.getPriceVolumeByPrice(10));
		assertNull(list.getPriceVolumeByPrice(20));
		assertNull(list.getPriceVolumeByPrice(30));
	}

	@Test
	@DisplayName("should contain one element after adding it to empty list")
	void test36() {
		OrderedLinkedList list = new OrderedLinkedList(false);

		list.setAdd(1, 10);

		assertEquals(1, list.getSize());
		assertFalse(list.isEmpty());

		Node node = list.iterator().next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("should contain two elements after adding them to empty list")
	void test37() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 2 }, new int[] { 20 });

		list.setAdd(1, 10);

		assertEquals(2, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(20, node.getVolume());
		node = i.next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("should change value of second element")
	void test38() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 2 }, new int[] { 20 });

		list.setAdd(2, 10);

		assertEquals(1, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("should add element to the tail of the list")
	void test39() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 5, 4, 3, 2 }, new int[] { 50, 40, 30, 20 });

		list.setAdd(1, 10);

		assertEquals(5, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(5, node.getOrder());
		assertEquals(50, node.getVolume());
		node = i.next();
		assertEquals(4, node.getOrder());
		assertEquals(40, node.getVolume());
		node = i.next();
		assertEquals(3, node.getOrder());
		assertEquals(30, node.getVolume());
		node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(20, node.getVolume());
		node = i.next();
		assertEquals(1, node.getOrder());
		assertEquals(10, node.getVolume());
	}

	@Test
	@DisplayName("should add element amidst the list")
	void test40() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 5, 4, 2 }, new int[] { 50, 40, 20 });

		list.setAdd(3, 30);

		assertEquals(4, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(5, node.getOrder());
		assertEquals(50, node.getVolume());
		node = i.next();
		assertEquals(4, node.getOrder());
		assertEquals(40, node.getVolume());
		node = i.next();
		assertEquals(3, node.getOrder());
		assertEquals(30, node.getVolume());
		node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(20, node.getVolume());
	}

	@Test
	@DisplayName("should add element amidst the list")
	void test41() {
		OrderedLinkedList list = new OrderedLinkedList(false, new int[] { 5, 4, 2 }, new int[] { 50, 40, 20 });

		list.setAdd(5, 100);
		list.setAdd(4, 100);
		list.setAdd(2, 100);

		assertEquals(3, list.getSize());
		assertFalse(list.isEmpty());

		Iterator<Node> i = list.iterator();
		Node node = i.next();
		assertEquals(5, node.getOrder());
		assertEquals(100, node.getVolume());
		node = i.next();
		assertEquals(4, node.getOrder());
		assertEquals(100, node.getVolume());
		node = i.next();
		assertEquals(2, node.getOrder());
		assertEquals(100, node.getVolume());
	}

}
