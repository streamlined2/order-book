package com.streamlined.orderbook.hashtableimplementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.streamlined.orderbook.arrayimplementation.ArrayContainer;
import com.streamlined.orderbook.arrayimplementation.NaturalOrderContainer;

class HashtableContainerPerformanceTest {

	@Test
	@DisplayName("measure search time for ask container")
	void testMeasureSearchTimeForAskContainer() {
		// setup
		final int capacity = 10_000_000;
		HashtableContainer container = new AskContainer(capacity);
		int price = 1;
		int volume = 1;
		for (int k = 0; k < capacity; k++) {
			container.set(price, volume);
			price += 1;
			volume += 10;
		}
		// measure
		long total = 0;
		price = 1;
		long start = System.currentTimeMillis();
		for (int k = 0; k < capacity; k++) {
			total += container.getVolumeByPrice(price);
			price += 1;
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for search in ask container = %d, total = %d%n".formatted(time, total));
	}

	@Test
	@DisplayName("measure search time for bid container")
	void testMeasureSearchTimeForBidContainer() {
		// setup
		final int capacity = 10_000_000;
		HashtableContainer container = new BidContainer(capacity);
		int price = 1;
		int volume = 1;
		for (int k = 0; k < capacity; k++) {
			container.set(price, volume);
			price += 1;
			volume += 10;
		}
		// measure
		long total = 0;
		price = 1;
		long start = System.currentTimeMillis();
		for (int k = 0; k < capacity; k++) {
			total += container.getVolumeByPrice(price);
			price += 1;
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for search in bid container = %d, total = %d%n".formatted(time, total));
	}

	@Test
	@DisplayName("measure insertion time for ask container")
	void testMeasureInsertionTimeForAskContainer() {
		final int count = 10_000_000;
		final int step = 10;
		// setup
		HashtableContainer container = new AskContainer(count);
		int price = 1;
		for (int k = 0; k < count; k++) {
			container.set(price, k % step == 0 ? 0 : 1);
			price += 2;
		}
		// measure
		price = 2;
		long start = System.currentTimeMillis();
		for (int k = 0; k < count; k += step) {
			container.set(price, 2);
			price += 2;
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for insertion in ask container = %d%n".formatted(time));
	}

}
