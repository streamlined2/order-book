package com.streamlined.orderbook.hashtableimplementation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderedArrayListTest {

	@Test
	@DisplayName("should create empty list")
	void test1() {
		OrderedArrayList list = new OrderedArrayList(true, 1);

		assertTrue(list.isEmpty());
		assertFalse(list.isFull());
		assertEquals(0, list.getSize());
	}

	@Test
	@DisplayName("should create list with one element")
	void test2() {
		OrderedArrayList list = new OrderedArrayList(true, 1);

		int index = list.setAdd(100, 1);

		assertFalse(list.isEmpty());
		assertTrue(list.isFull());
		assertEquals(1, list.getSize());
		assertEquals(0, index);
	}

	@Test
	@DisplayName("should create list with given elements")
	void test3() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 100, 200, 300 }, new int[] { 1, 2, 3 });

		assertFalse(list.isEmpty());
		assertTrue(list.isFull());
		assertEquals(3, list.getSize());
		assertEquals(100, list.getPrice(0));
		assertEquals(1, list.getVolume(0));
		assertEquals(200, list.getPrice(1));
		assertEquals(2, list.getVolume(1));
		assertEquals(300, list.getPrice(2));
		assertEquals(3, list.getVolume(2));
	}

	@Test
	@DisplayName("should change value of element")
	void test4() {
		OrderedArrayList list = new OrderedArrayList(true, 1);

		list.setAdd(100, 1);
		int index = list.setAdd(100, 3);

		assertFalse(list.isEmpty());
		assertTrue(list.isFull());
		assertEquals(1, list.getSize());
		assertEquals(0, index);
		assertEquals(100, list.getPrice(0));
		assertEquals(3, list.getVolume(0));
	}

	@Test
	@DisplayName("should not find value of element for full list")
	void test5() {
		OrderedArrayList list = new OrderedArrayList(true, 1);

		list.setAdd(100, 1);
		int index = list.setAdd(200, 3);

		assertFalse(list.isEmpty());
		assertTrue(list.isFull());
		assertEquals(1, list.getSize());
		assertEquals(List.PRICE_NOT_FOUND, index);
		assertEquals(100, list.getPrice(0));
		assertEquals(1, list.getVolume(0));
	}

	@Test
	@DisplayName("should add second element to list after first element")
	void test6() {
		OrderedArrayList list = new OrderedArrayList(true, 2);

		list.setAdd(100, 1);
		int index = list.setAdd(200, 2);

		assertFalse(list.isEmpty());
		assertTrue(list.isFull());
		assertEquals(2, list.getSize());
		assertEquals(1, index);
		assertEquals(100, list.getPrice(0));
		assertEquals(1, list.getVolume(0));
		assertEquals(200, list.getPrice(1));
		assertEquals(2, list.getVolume(1));
	}

	@Test
	@DisplayName("should add second element to list before first element")
	void test7() {
		OrderedArrayList list = new OrderedArrayList(true, 2);

		list.setAdd(200, 2);
		int index = list.setAdd(100, 1);

		assertFalse(list.isEmpty());
		assertTrue(list.isFull());
		assertEquals(2, list.getSize());
		assertEquals(0, index);
		assertEquals(100, list.getPrice(0));
		assertEquals(1, list.getVolume(0));
		assertEquals(200, list.getPrice(1));
		assertEquals(2, list.getVolume(1));
	}

	@Test
	@DisplayName("should return false if price not found")
	void test8() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 100, 200, 300 }, new int[] { 1, 2, 3 });

		boolean removed = list.remove(400);

		assertFalse(removed);
		assertEquals(100, list.getPrice(0));
		assertEquals(1, list.getVolume(0));
		assertEquals(200, list.getPrice(1));
		assertEquals(2, list.getVolume(1));
		assertEquals(300, list.getPrice(2));
		assertEquals(3, list.getVolume(2));
	}

	@Test
	@DisplayName("should return true and remove last element if price found")
	void test9() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 100, 200, 300 }, new int[] { 1, 2, 3 });

		boolean removed = list.remove(300);

		assertTrue(removed);
		assertEquals(2, list.getSize());
		assertEquals(100, list.getPrice(0));
		assertEquals(1, list.getVolume(0));
		assertEquals(200, list.getPrice(1));
		assertEquals(2, list.getVolume(1));
	}

	@Test
	@DisplayName("should return true and remove middle element if price found")
	void test10() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 100, 200, 300 }, new int[] { 1, 2, 3 });

		boolean removed = list.remove(200);

		assertTrue(removed);
		assertEquals(2, list.getSize());
		assertEquals(100, list.getPrice(0));
		assertEquals(1, list.getVolume(0));
		assertEquals(300, list.getPrice(1));
		assertEquals(3, list.getVolume(1));
	}

	@Test
	@DisplayName("should return true and remove first element if price found")
	void test11() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 100, 200, 300 }, new int[] { 1, 2, 3 });

		boolean removed = list.remove(100);

		assertTrue(removed);
		assertEquals(2, list.getSize());
		assertEquals(200, list.getPrice(0));
		assertEquals(2, list.getVolume(0));
		assertEquals(300, list.getPrice(1));
		assertEquals(3, list.getVolume(1));
	}

	@Test
	@DisplayName("should return null if price not found")
	void test12() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 100, 200, 300 }, new int[] { 1, 2, 3 });

		assertNull(list.getPriceVolumeByPrice(400));
		assertNull(list.getPriceVolumeByPrice(50));
		assertNull(list.getPriceVolumeByPrice(150));
		assertEquals(100, list.getPrice(0));
		assertEquals(1, list.getVolume(0));
		assertEquals(200, list.getPrice(1));
		assertEquals(2, list.getVolume(1));
		assertEquals(300, list.getPrice(2));
		assertEquals(3, list.getVolume(2));
	}

	@Test
	@DisplayName("should return price and size if price found")
	void test13() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 100, 200, 300 }, new int[] { 1, 2, 3 });

		assertEquals(100, list.getPriceVolumeByPrice(100).getPrice());
		assertEquals(1, list.getPriceVolumeByPrice(100).getVolume());
		assertEquals(200, list.getPriceVolumeByPrice(200).getPrice());
		assertEquals(2, list.getPriceVolumeByPrice(200).getVolume());
		assertEquals(300, list.getPriceVolumeByPrice(300).getPrice());
		assertEquals(3, list.getPriceVolumeByPrice(300).getVolume());
	}

	@Test
	@DisplayName("should return first non-empty element for ascending list")
	void test14() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 100, 200, 300 }, new int[] { 0, 2, 3 });

		assertEquals(200, list.getFirstNonEmptyNode().getPrice());
		assertEquals(2, list.getFirstNonEmptyNode().getVolume());
	}

	@Test
	@DisplayName("should return first non-empty element for descending list")
	void test15() {
		OrderedArrayList list = new OrderedArrayList(false, new int[] { 100, 200, 300 }, new int[] { 1, 2, 0 });

		assertEquals(200, list.getFirstNonEmptyNode().getPrice());
		assertEquals(2, list.getFirstNonEmptyNode().getVolume());
	}

	@Test
	@DisplayName("should return null if non-empty element absent for ascending list")
	void test16() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 100, 200, 300 }, new int[] { 0, 0, 0 });

		assertNull(list.getFirstNonEmptyNode());
	}

	@Test
	@DisplayName("should return null if non-empty element absent for descending list")
	void test17() {
		OrderedArrayList list = new OrderedArrayList(false, new int[] { 100, 200, 300 }, new int[] { 0, 0, 0 });

		assertNull(list.getFirstNonEmptyNode());
	}

	@Test
	@DisplayName("should be no subtracted volume for empty list")
	void test18() {
		OrderedArrayList list = new OrderedArrayList(false, 1);

		assertEquals(0, list.subtractVolume(100).subtractedVolume);
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty ascending list")
	void test19() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(5, list.subtractVolume(5).subtractedVolume);
		assertEquals(5, list.getPriceVolumeByPrice(10).getVolume());
		assertEquals(20, list.getPriceVolumeByPrice(20).getVolume());
		assertEquals(30, list.getPriceVolumeByPrice(30).getVolume());
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty ascending list")
	void test20() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(15, list.subtractVolume(15).subtractedVolume);
		assertNull(list.getPriceVolumeByPrice(10));
		assertEquals(15, list.getPriceVolumeByPrice(20).getVolume());
		assertEquals(30, list.getPriceVolumeByPrice(30).getVolume());
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty ascending list")
	void test21() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(35, list.subtractVolume(35).subtractedVolume);
		assertNull(list.getPriceVolumeByPrice(10));
		assertNull(list.getPriceVolumeByPrice(20));
		assertEquals(25, list.getPriceVolumeByPrice(30).getVolume());
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty ascending list")
	void test22() {
		OrderedArrayList list = new OrderedArrayList(true, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(60, list.subtractVolume(65).subtractedVolume);
		assertNull(list.getPriceVolumeByPrice(10));
		assertNull(list.getPriceVolumeByPrice(20));
		assertNull(list.getPriceVolumeByPrice(30));
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty descending list")
	void test23() {
		OrderedArrayList list = new OrderedArrayList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(5, list.subtractVolume(5).subtractedVolume);
		assertEquals(10, list.getPriceVolumeByPrice(10).getVolume());
		assertEquals(20, list.getPriceVolumeByPrice(20).getVolume());
		assertEquals(25, list.getPriceVolumeByPrice(30).getVolume());
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty descending list")
	void test24() {
		OrderedArrayList list = new OrderedArrayList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(35, list.subtractVolume(35).subtractedVolume);
		assertEquals(10, list.getPriceVolumeByPrice(10).getVolume());
		assertEquals(15, list.getPriceVolumeByPrice(20).getVolume());
		assertNull(list.getPriceVolumeByPrice(30));
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty descending list")
	void test25() {
		OrderedArrayList list = new OrderedArrayList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(55, list.subtractVolume(55).subtractedVolume);
		assertEquals(5, list.getPriceVolumeByPrice(10).getVolume());
		assertNull(list.getPriceVolumeByPrice(20));
		assertNull(list.getPriceVolumeByPrice(30));
	}

	@Test
	@DisplayName("should be correct subtracted volume for non-empty descending list")
	void test26() {
		OrderedArrayList list = new OrderedArrayList(false, new int[] { 10, 20, 30 }, new int[] { 10, 20, 30 });

		assertEquals(60, list.subtractVolume(65).subtractedVolume);
		assertNull(list.getPriceVolumeByPrice(10));
		assertNull(list.getPriceVolumeByPrice(20));
		assertNull(list.getPriceVolumeByPrice(30));
	}

}
