package com.streamlined.orderbook.hashtableimplementation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashtableContainerTest {

	@Test
	@DisplayName("check value of first price group start index which should remain fixed")
	void test1() {
		HashtableContainer container = new HashtableContainer();

		assertEquals(8, container.defineFirstPriceGroupStart(11));
		assertEquals(8, container.defineFirstPriceGroupStart(12));
		assertEquals(0, container.getSize());
		assertTrue(container.isEmpty());
	}

	@Test
	@DisplayName("check mapping of price to group index for growing price")
	void test2() {
		HashtableContainer container = new HashtableContainer(2);
		container.defineFirstPriceGroupStart(11);

		assertEquals(0, container.mapPriceToGroupIndex(8));
		assertEquals(0, container.mapPriceToGroupIndex(9));
		assertEquals(0, container.mapPriceToGroupIndex(10));
		assertEquals(0, container.mapPriceToGroupIndex(11));

		assertEquals(1, container.mapPriceToGroupIndex(12));
		assertEquals(1, container.mapPriceToGroupIndex(13));
		assertEquals(1, container.mapPriceToGroupIndex(14));
		assertEquals(1, container.mapPriceToGroupIndex(15));

		assertEquals(2, container.mapPriceToGroupIndex(16));
	}

	@Test
	@DisplayName("check mapping of price to group index for lowering price")
	void test3() {
		HashtableContainer container = new HashtableContainer(2);
		container.defineFirstPriceGroupStart(11);

		assertEquals(1, container.mapPriceToGroupIndex(7));
		assertEquals(1, container.mapPriceToGroupIndex(6));
		assertEquals(1, container.mapPriceToGroupIndex(5));
		assertEquals(1, container.mapPriceToGroupIndex(4));

		assertEquals(0, container.mapPriceToGroupIndex(3));

		assertEquals(-1, container.mapPriceToGroupIndex(-1));
	}

	@Test
	@DisplayName("check mapping of price to group index for lowering and growing price")
	void test4() {
		HashtableContainer container = new HashtableContainer(3);
		container.defineFirstPriceGroupStart(11);

		assertEquals(0, container.mapPriceToGroupIndex(8));
		assertEquals(0, container.mapPriceToGroupIndex(9));
		assertEquals(0, container.mapPriceToGroupIndex(10));
		assertEquals(0, container.mapPriceToGroupIndex(11));

		assertEquals(1, container.mapPriceToGroupIndex(12));
		assertEquals(1, container.mapPriceToGroupIndex(13));
		assertEquals(1, container.mapPriceToGroupIndex(14));
		assertEquals(1, container.mapPriceToGroupIndex(15));

		assertEquals(2, container.mapPriceToGroupIndex(7));
		assertEquals(2, container.mapPriceToGroupIndex(6));
		assertEquals(2, container.mapPriceToGroupIndex(5));
		assertEquals(2, container.mapPriceToGroupIndex(4));

		assertEquals(1, container.mapPriceToGroupIndex(3));
	}

	@Test
	@DisplayName("check indices for prices if they grow up")
	void test5() {
		HashtableContainer container = new HashtableContainer(3);

		assertEquals(0, container.locateGroupForPrice(5));
		assertEquals(container.getCapacity(), container.getMinPriceGroupIndex());
		assertEquals(0, container.getMaxPriceGroupIndex());
		assertEquals(3, container.getCapacity());

		assertEquals(1, container.locateGroupForPrice(9));
		assertEquals(container.getCapacity(), container.getMinPriceGroupIndex());
		assertEquals(1, container.getMaxPriceGroupIndex());
		assertEquals(3, container.getCapacity());

		assertEquals(2, container.locateGroupForPrice(13));
		assertEquals(container.getCapacity(), container.getMinPriceGroupIndex());
		assertEquals(2, container.getMaxPriceGroupIndex());
		assertEquals(3, container.getCapacity());

		assertEquals(3, container.locateGroupForPrice(17));
		assertEquals(container.getCapacity(), container.getMinPriceGroupIndex());
		assertEquals(3, container.getMaxPriceGroupIndex());
		assertTrue(container.getCapacity() > 3);
	}

	@Test
	@DisplayName("check indices for prices if they decrease down")
	void test6() {
		HashtableContainer container = new HashtableContainer(3);

		assertEquals(0, container.locateGroupForPrice(20));
		assertEquals(container.getCapacity(), container.getMinPriceGroupIndex());
		assertEquals(0, container.getMaxPriceGroupIndex());
		assertEquals(3, container.getCapacity());

		assertEquals(2, container.locateGroupForPrice(16));
		assertEquals(2, container.getMinPriceGroupIndex());
		assertEquals(0, container.getMaxPriceGroupIndex());
		assertEquals(3, container.getCapacity());

		assertEquals(1, container.locateGroupForPrice(12));
		assertEquals(1, container.getMinPriceGroupIndex());
		assertEquals(0, container.getMaxPriceGroupIndex());
		assertEquals(3, container.getCapacity());

		assertEquals(1, container.locateGroupForPrice(8));
		assertEquals(container.getCapacity() - 3, container.getMinPriceGroupIndex());
		assertEquals(0, container.getMaxPriceGroupIndex());
		assertTrue(container.getCapacity() > 3);
	}

	@Test
	@DisplayName("check indices for prices if they increase up and decrease down interchangeably")
	void test7() {
		HashtableContainer container = new HashtableContainer(2);

		assertEquals(container.getCapacity(), container.getMinPriceGroupIndex());
		assertEquals(-1, container.getMaxPriceGroupIndex());

		assertEquals(0, container.locateGroupForPrice(20));
		assertEquals(2, container.getMinPriceGroupIndex());
		assertEquals(0, container.getMaxPriceGroupIndex());
		assertEquals(2, container.getCapacity());

		assertEquals(1, container.locateGroupForPrice(16));
		assertEquals(1, container.getMinPriceGroupIndex());
		assertEquals(0, container.getMaxPriceGroupIndex());
		assertEquals(2, container.getCapacity());

		int index = container.locateGroupForPrice(12);
		assertEquals(container.getCapacity() - 2, index);
		assertEquals(container.getCapacity() - 2, container.getMinPriceGroupIndex());
		assertEquals(0, container.getMaxPriceGroupIndex());
		assertTrue(container.getCapacity() > 2);

		index = container.locateGroupForPrice(8);
		assertEquals(container.getCapacity() - 3, index);
		assertEquals(container.getCapacity() - 3, container.getMinPriceGroupIndex());
		assertEquals(0, container.getMaxPriceGroupIndex());
		assertTrue(container.getCapacity() > 3);
	}

	@Test
	@DisplayName("check method adds new element and sets its size")
	void test8() {
		HashtableContainer container = new HashtableContainer(1);

		container.set(10, 1);
		container.set(11, 1);
		assertEquals(2, container.getSize());

		container.set(12, 1);
		container.set(13, 1);
		assertEquals(4, container.getSize());

		container.set(30, 3);
		container.set(31, 3);
		assertEquals(6, container.getSize());

		container.set(32, 3);
		container.set(33, 3);
		assertEquals(8, container.getSize());
	}

	@Test
	@DisplayName("empty container does not contain any element")
	void test9() {
		HashtableContainer container = new HashtableContainer();

		assertEquals(HashtableContainer.VOLUME_VALUE_ABSENT, container.getVolumeByPrice(100));
		assertEquals(HashtableContainer.VOLUME_VALUE_ABSENT, container.getVolumeByPrice(200));
		assertEquals(HashtableContainer.VOLUME_VALUE_ABSENT, container.getVolumeByPrice(300));
	}

	@Test
	@DisplayName("non-empty container should contain added earlier element")
	void test10() {
		HashtableContainer container = new HashtableContainer();

		container.set(100, 1);
		container.set(200, 2);
		container.set(300, 3);

		assertEquals(1, container.getVolumeByPrice(100));
		assertEquals(2, container.getVolumeByPrice(200));
		assertEquals(3, container.getVolumeByPrice(300));
	}

}
