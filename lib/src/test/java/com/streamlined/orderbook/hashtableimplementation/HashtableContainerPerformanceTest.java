package com.streamlined.orderbook.hashtableimplementation;

import java.security.SecureRandom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

	@Test
	@DisplayName("measure volume subtraction time for best price")
	void testVolumeSubtractionForBestPrice() {
		SecureRandom random = new SecureRandom();
		final int count = 1_000_000;
		// setup
		HashtableContainer container = new AskContainer(count);
		int price = 1;
		for (int k = 0; k < count; k++) {
			container.set(price, random.nextInt(1000) + 1);
			price++;
		}
		// measure
		final int buyout = 100;
		final int times = 10_000;
		long start = System.currentTimeMillis();
		for (int k = 0; k < times; k++) {
			container.subtractVolumeForBestPrice(buyout);
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for volume subtraction for best price = %d%n".formatted(time));
	}

	@Test
	@DisplayName("measure best price calculation after huge buyout")
	void testBestPriceCalculation() {
		SecureRandom random = new SecureRandom();
		final int maxPrice = 1_000_000;
		final int maxVolume = 100;
		// setup
		HashtableContainer container = new AskContainer(maxPrice);
		for (int price = 0; price < maxPrice; price++) {
			container.set(price, random.nextInt(maxVolume) + 1);
		}
		// measure
		final int count = 100;
		final int buyout = maxPrice * maxVolume;
		container.subtractVolumeForBestPrice(buyout);
		long start = System.currentTimeMillis();
		long total = 0;
		for (int k = 0; k < count; k++) {
			total += container.getBestPrice();
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for best price calculation = %d%n".formatted(time));
	}

}
