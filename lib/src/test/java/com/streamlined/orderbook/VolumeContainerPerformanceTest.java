package com.streamlined.orderbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.streamlined.orderbook.arrayimplementation.ArrayContainer;
import com.streamlined.orderbook.arrayimplementation.NaturalOrderContainer;
import com.streamlined.orderbook.arrayimplementation.ReversedOrderContainer;

class VolumeContainerPerformanceTest {

	@Test
	@DisplayName("measure search time for non-reversed container")
	void testMeasureSearchTimeForNonReversedContainer() {
		// setup
		final int capacity = 10_000_000;
		ArrayContainer container = new NaturalOrderContainer(capacity);
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
		for (int k = 0; k < container.getSize(); k++) {
			total += container.getVolumeByPrice(price);
			price += 1;
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for search in non-reversed container = %d, total = %d%n".formatted(time, total));
	}

	@Test
	@DisplayName("measure search time for reversed container")
	void testMeasureSearchTimeForReversedContainer() {
		// setup
		final int capacity = 10_000_000;
		ArrayContainer container = new ReversedOrderContainer(capacity);
		int price = capacity;
		int volume = 1;
		for (int k = 0; k < capacity; k++) {
			container.set(price, volume);
			price -= 1;
			volume += 10;
		}
		// measure
		long total = 0;
		price = 1;
		long start = System.currentTimeMillis();
		for (int k = 0; k < container.getSize(); k++) {
			total += container.getVolumeByPrice(price);
			price += 1;
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for search in reversed container = %d, total = %d%n".formatted(time, total));
	}

	@Test
	@DisplayName("measure insertion time for non-reversed container")
	void testMeasureInsertionTimeForNonReversedContainer() {
		final int count = 10_000_000;
		final int step = 10;
		// setup
		ArrayContainer container = new NaturalOrderContainer(count);
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
		System.out.print("time for insertion in non-reversed container = %d%n".formatted(time));
	}

}
