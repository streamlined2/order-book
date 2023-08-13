package com.streamlined.orderbook.hashtableimplementation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashtableContainerTest {

	@Test
	@DisplayName("check value of first price group start index which should remain fixed")
	void test1() {
		HashtableContainer container = new HashtableContainer();

		assertEquals(8, container.defineFirstPriceGroupStart(11, 2));
		assertEquals(8, container.defineFirstPriceGroupStart(12, 2));
		assertEquals(0, container.getSize());
		assertTrue(container.isEmpty());
	}

	@Test
	@DisplayName("check mapping of price to group index for growing price")
	void test2() {
		HashtableContainer container = new HashtableContainer(2);
		container.defineFirstPriceGroupStart(11, 2);

		assertEquals(0, container.mapPriceToGroupIndex(8, 2));
		assertEquals(0, container.mapPriceToGroupIndex(9, 2));
		assertEquals(0, container.mapPriceToGroupIndex(10, 2));
		assertEquals(0, container.mapPriceToGroupIndex(11, 2));

		assertEquals(1, container.mapPriceToGroupIndex(12, 2));
		assertEquals(1, container.mapPriceToGroupIndex(13, 2));
		assertEquals(1, container.mapPriceToGroupIndex(14, 2));
		assertEquals(1, container.mapPriceToGroupIndex(15, 2));

		assertEquals(2, container.mapPriceToGroupIndex(16, 2));
	}

	@Test
	@DisplayName("check mapping of price to group index for lowering price")
	void test3() {
		HashtableContainer container = new HashtableContainer(2);
		container.defineFirstPriceGroupStart(11, 2);

		assertEquals(1, container.mapPriceToGroupIndex(7, 2));
		assertEquals(1, container.mapPriceToGroupIndex(6, 2));
		assertEquals(1, container.mapPriceToGroupIndex(5, 2));
		assertEquals(1, container.mapPriceToGroupIndex(4, 2));

		assertEquals(0, container.mapPriceToGroupIndex(3, 2));

		assertEquals(-1, container.mapPriceToGroupIndex(-1, 2));
	}

	@Test
	@DisplayName("check mapping of price to group index for lowering and growing price")
	void test4() {
		HashtableContainer container = new HashtableContainer(3);
		container.defineFirstPriceGroupStart(11, 2);

		assertEquals(0, container.mapPriceToGroupIndex(8, 2));
		assertEquals(0, container.mapPriceToGroupIndex(9, 2));
		assertEquals(0, container.mapPriceToGroupIndex(10, 2));
		assertEquals(0, container.mapPriceToGroupIndex(11, 2));

		assertEquals(1, container.mapPriceToGroupIndex(12, 2));
		assertEquals(1, container.mapPriceToGroupIndex(13, 2));
		assertEquals(1, container.mapPriceToGroupIndex(14, 2));
		assertEquals(1, container.mapPriceToGroupIndex(15, 2));

		assertEquals(2, container.mapPriceToGroupIndex(7, 2));
		assertEquals(2, container.mapPriceToGroupIndex(6, 2));
		assertEquals(2, container.mapPriceToGroupIndex(5, 2));
		assertEquals(2, container.mapPriceToGroupIndex(4, 2));
		assertFalse(container.needsExpansion(7, 2));

		assertEquals(1, container.mapPriceToGroupIndex(3, 2));
	}

}
