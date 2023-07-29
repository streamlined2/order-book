package com.streamlined.orderbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VolumeContainerTest {

	@Test
	@DisplayName("initial capacity should be 1 and size should be 0 for empty container created with initial capacity 0")
	void test1() {
		VolumeContainer container = new VolumeContainer(0);

		assertEquals(1, container.getCapacity());
		assertEquals(0, container.getSize());
	}

	@Test
	@DisplayName("initial capacity should be 1 and size 0 after container created with initial capacity 1")
	void test2() {
		VolumeContainer container = new VolumeContainer(1);

		assertEquals(1, container.getCapacity());
		assertEquals(0, container.getSize());
	}

	@Test
	@DisplayName("initial capacity should be 1 and size 1 after one element added to container with initial capacity 1")
	void test3() {
		VolumeContainer container = new VolumeContainer(1);
		final int price = 1000;
		final int volume = 100;
		container.add(price, volume);

		assertEquals(1, container.getCapacity());
		assertEquals(1, container.getSize());
		assertEquals(price, container.getPrice(0));
		assertEquals(volume, container.getVolume(0));
	}

	@Test
	@DisplayName("initial capacity should be greater 1 and size 2 after two elements added to container with initial capacity 1")
	void test4() {
		VolumeContainer container = new VolumeContainer(1);
		final int price1 = 1000;
		final int volume1 = 100;
		container.add(price1, volume1);
		final int price2 = 2000;
		final int volume2 = 300;
		container.add(price2, volume2);

		assertTrue(container.getCapacity() > 1);
		assertEquals(2, container.getSize());
		assertEquals(price1, container.getPrice(0));
		assertEquals(volume1, container.getVolume(0));
		assertEquals(price2, container.getPrice(1));
		assertEquals(volume2, container.getVolume(1));
	}

}
