package com.streamlined.orderbook.hashtableimplementation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.streamlined.orderbook.PriceVolume;

class HashtableContainerTest {

	@Test
	@DisplayName("check method adds new element and sets its size")
	void test8() {
		HashtableContainer container = new BidContainer(1);

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
		HashtableContainer container = new BidContainer();

		assertEquals(0, container.getVolumeByPrice(100));
		assertEquals(0, container.getVolumeByPrice(200));
		assertEquals(0, container.getVolumeByPrice(300));
	}

	@Test
	@DisplayName("non-empty container should contain elements added earlier")
	void test10() {
		HashtableContainer container = new BidContainer();

		container.set(100, 1);
		container.set(200, 2);
		container.set(300, 3);

		assertEquals(1, container.getVolumeByPrice(100));
		assertEquals(2, container.getVolumeByPrice(200));
		assertEquals(3, container.getVolumeByPrice(300));
	}

	@Test
	@DisplayName("check best price/volume for bid container if values added in ascending order")
	void test11() {
		HashtableContainer container = new BidContainer();

		container.set(100, 1);
		container.set(101, 2);
		container.set(102, 3);
		container.set(103, 4);
		container.set(104, 5);
		container.set(105, 6);
		container.set(106, 7);
		container.set(107, 8);
		container.set(108, 9);

		PriceVolume priceVolume = container.getBestPriceVolume();
		assertEquals(108, priceVolume.getPrice());
		assertEquals(9, priceVolume.getVolume());
		assertEquals(108, container.getBestPrice());
	}

	@Test
	@DisplayName("check best price/volume for bid container if values added in ascending order")
	void test11a() {
		HashtableContainer container = new BidContainer();

		container.set(100, 0);
		container.set(101, 0);
		container.set(102, 0);
		container.set(103, 20);
		container.set(104, 10);
		container.set(105, 0);
		container.set(106, 0);
		container.set(107, 0);
		container.set(108, 0);

		PriceVolume priceVolume = container.getBestPriceVolume();
		assertEquals(104, priceVolume.getPrice());
		assertEquals(10, priceVolume.getVolume());
		assertEquals(104, container.getBestPrice());
	}

	@Test
	@DisplayName("check best price/volume for ask container if values added in ascending order")
	void test12() {
		HashtableContainer container = new AskContainer();

		container.set(100, 1);
		container.set(101, 2);
		container.set(102, 3);
		container.set(103, 4);
		container.set(104, 5);
		container.set(105, 6);
		container.set(106, 7);
		container.set(107, 8);
		container.set(108, 9);

		PriceVolume priceVolume = container.getBestPriceVolume();
		assertEquals(100, priceVolume.getPrice());
		assertEquals(1, priceVolume.getVolume());
		assertEquals(100, container.getBestPrice());
	}

	@Test
	@DisplayName("check best price/volume for ask container if values added in ascending order")
	void test12a() {
		HashtableContainer container = new AskContainer();

		container.set(100, 0);
		container.set(101, 0);
		container.set(102, 0);
		container.set(103, 0);
		container.set(104, 10);
		container.set(105, 20);
		container.set(106, 0);
		container.set(107, 0);
		container.set(108, 0);

		PriceVolume priceVolume = container.getBestPriceVolume();
		assertEquals(104, priceVolume.getPrice());
		assertEquals(10, priceVolume.getVolume());
		assertEquals(104, container.getBestPrice());
	}

	@Test
	@DisplayName("should not be best price/volume for empty bid container")
	void test13() {
		HashtableContainer container = new BidContainer();

		PriceVolume priceVolume = container.getBestPriceVolume();
		assertNull(priceVolume);
		assertEquals(HashtableContainer.PRICE_VALUE_ABSENT, container.getBestPrice());
	}

	@Test
	@DisplayName("should not be best price/volume for empty ask container")
	void test14() {
		HashtableContainer container = new AskContainer();

		PriceVolume priceVolume = container.getBestPriceVolume();
		assertNull(priceVolume);
		assertEquals(HashtableContainer.PRICE_VALUE_ABSENT, container.getBestPrice());
	}

	@Test
	@DisplayName("check best price/volume for bid container if values added in descending order")
	void test15() {
		HashtableContainer container = new BidContainer();

		container.set(108, 9);
		container.set(107, 8);
		container.set(106, 7);
		container.set(105, 6);
		container.set(104, 5);
		container.set(103, 4);
		container.set(102, 3);
		container.set(101, 2);
		container.set(100, 1);

		PriceVolume priceVolume = container.getBestPriceVolume();
		assertEquals(108, priceVolume.getPrice());
		assertEquals(9, priceVolume.getVolume());
		assertEquals(108, container.getBestPrice());
	}

	@Test
	@DisplayName("check best price/volume for ask container if values added in descending order")
	void test16() {
		HashtableContainer container = new AskContainer();

		container.set(100, 1);
		container.set(101, 2);
		container.set(102, 3);
		container.set(103, 4);
		container.set(104, 5);
		container.set(105, 6);
		container.set(106, 7);
		container.set(107, 8);
		container.set(108, 9);

		PriceVolume priceVolume = container.getBestPriceVolume();
		assertEquals(100, priceVolume.getPrice());
		assertEquals(1, priceVolume.getVolume());
		assertEquals(100, container.getBestPrice());
	}

	@Test
	@DisplayName("best price volume subtraction for empty bid container should not produce result")
	void test17() {
		HashtableContainer container = new BidContainer();

		assertEquals(0, container.subtractVolumeForBestPrice(100).getVolumeSubtracted());
	}

	@Test
	@DisplayName("best price volume subtraction for non-empty bid container should decrease volumes")
	void test18() {
		HashtableContainer container = new BidContainer();

		container.set(100, 1);
		container.set(101, 2);
		container.set(102, 3);
		container.set(103, 4);

		assertEquals(2, container.subtractVolumeForBestPrice(2).getVolumeSubtracted());
		assertEquals(1, container.getVolumeByPrice(100));
		assertEquals(2, container.getVolumeByPrice(101));
		assertEquals(3, container.getVolumeByPrice(102));
		assertEquals(2, container.getVolumeByPrice(103));
	}

	@Test
	@DisplayName("best price volume subtraction for non-empty bid container should decrease volumes")
	void test19() {
		HashtableContainer container = new BidContainer();

		container.set(100, 1);
		container.set(101, 2);
		container.set(102, 3);
		container.set(103, 4);

		assertEquals(6, container.subtractVolumeForBestPrice(6).getVolumeSubtracted());
		assertEquals(1, container.getVolumeByPrice(100));
		assertEquals(2, container.getVolumeByPrice(101));
		assertEquals(1, container.getVolumeByPrice(102));
		assertEquals(0, container.getVolumeByPrice(103));
	}

	@Test
	@DisplayName("best price volume subtraction for non-empty bid container should decrease volumes")
	void test20() {
		HashtableContainer container = new BidContainer();

		container.set(100, 1);
		container.set(101, 2);
		container.set(102, 3);
		container.set(103, 4);

		assertEquals(10, container.subtractVolumeForBestPrice(12).getVolumeSubtracted());
		assertEquals(0, container.getVolumeByPrice(100));
		assertEquals(0, container.getVolumeByPrice(101));
		assertEquals(0, container.getVolumeByPrice(102));
		assertEquals(0, container.getVolumeByPrice(103));
	}

	@Test
	@DisplayName("best price volume subtraction for empty ask container should not produce result")
	void test21() {
		HashtableContainer container = new AskContainer();

		assertEquals(0, container.subtractVolumeForBestPrice(100).getVolumeSubtracted());
	}

	@Test
	@DisplayName("best price volume subtraction for non-empty ask container should decrease volumes")
	void test22() {
		HashtableContainer container = new AskContainer();

		container.set(100, 1);
		container.set(101, 2);
		container.set(102, 3);
		container.set(103, 4);

		assertEquals(1, container.subtractVolumeForBestPrice(1).getVolumeSubtracted());
		assertEquals(0, container.getVolumeByPrice(100));
		assertEquals(2, container.getVolumeByPrice(101));
		assertEquals(3, container.getVolumeByPrice(102));
		assertEquals(4, container.getVolumeByPrice(103));
	}

	@Test
	@DisplayName("best price volume subtraction for non-empty ask container should decrease volumes")
	void test23() {
		HashtableContainer container = new AskContainer();

		container.set(100, 1);
		container.set(101, 2);
		container.set(102, 3);
		container.set(103, 4);

		assertEquals(4, container.subtractVolumeForBestPrice(4).getVolumeSubtracted());
		assertEquals(0, container.getVolumeByPrice(100));
		assertEquals(0, container.getVolumeByPrice(101));
		assertEquals(2, container.getVolumeByPrice(102));
		assertEquals(4, container.getVolumeByPrice(103));
	}

	@Test
	@DisplayName("best price volume subtraction for non-empty ask container should decrease volumes")
	void test24() {
		HashtableContainer container = new AskContainer();

		container.set(100, 1);
		container.set(101, 2);
		container.set(102, 3);
		container.set(103, 4);

		assertEquals(10, container.subtractVolumeForBestPrice(12).getVolumeSubtracted());
		assertEquals(0, container.getVolumeByPrice(100));
		assertEquals(0, container.getVolumeByPrice(101));
		assertEquals(0, container.getVolumeByPrice(102));
		assertEquals(0, container.getVolumeByPrice(103));
	}

}
