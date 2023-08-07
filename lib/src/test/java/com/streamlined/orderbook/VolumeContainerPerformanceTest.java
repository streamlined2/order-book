package com.streamlined.orderbook;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VolumeContainerPerformanceTest {

	@Test
	@DisplayName("measure search time for non-reversed container")
	void testMeasureSearchTimeForNonReversedContainer() {
		// setup
		VolumeContainer container = new VolumeContainer(10_000_000);
		int price = 1;
		int volume = 1;
		for (int k = 0; k < container.getCapacity(); k++) {
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
		VolumeContainer container = new VolumeContainer(true, 10_000_000);
		int price = container.getCapacity();
		int volume = 1;
		for (int k = 0; k < container.getCapacity(); k++) {
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

}
