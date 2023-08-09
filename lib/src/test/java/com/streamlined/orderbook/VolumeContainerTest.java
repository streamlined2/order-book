package com.streamlined.orderbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VolumeContainerTest {

	@Test
	@DisplayName("initial capacity should be 1 and size should be 0 for empty container created with initial capacity 0")
	void test1() {
		ArrayContainer container = new NaturalOrderContainer(0);

		assertEquals(1, container.getCapacity());
		assertEquals(0, container.getSize());
	}

	@Test
	@DisplayName("initial capacity should be 1 and size 0 after container created with initial capacity 1")
	void test2() {
		ArrayContainer container = new NaturalOrderContainer(1);

		assertEquals(1, container.getCapacity());
		assertEquals(0, container.getSize());
	}

	@Test
	@DisplayName("initial capacity should be 1 and size 1 after one element added to container with initial capacity 1")
	void test3() {
		ArrayContainer container = new NaturalOrderContainer(1);
		final int price = 1000;
		final int volume = 100;
		container.set(price, volume);

		assertEquals(1, container.getCapacity());
		assertEquals(1, container.getSize());
		assertEquals(price, container.getPrice(0));
		assertEquals(volume, container.getVolume(0));
	}

	@Test
	@DisplayName("initial capacity should be greater 1 and size 2 after two elements added to container with initial capacity 1")
	void test4() {
		ArrayContainer container = new NaturalOrderContainer(1);
		final int price1 = 1000;
		final int volume1 = 100;
		container.set(price1, volume1);
		final int price2 = 2000;
		final int volume2 = 300;
		container.set(price2, volume2);

		assertTrue(container.getCapacity() > 1);
		assertEquals(2, container.getSize());
		assertEquals(price1, container.getPrice(0));
		assertEquals(volume1, container.getVolume(0));
		assertEquals(price2, container.getPrice(1));
		assertEquals(volume2, container.getVolume(1));
	}

	@Test
	@DisplayName("check binary search for price value if succeeds")
	void test5() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		assertEquals(0, container.indexOf(100));
		assertEquals(1, container.indexOf(200));
		assertEquals(2, container.indexOf(300));
	}

	@Test
	@DisplayName("check binary search for price value if fails")
	void test6() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		assertEquals(-1, container.indexOf(0));
		assertEquals(-2, container.indexOf(150));
		assertEquals(-3, container.indexOf(250));
		assertEquals(-4, container.indexOf(350));
	}

	@Test
	@DisplayName("should copy elements up to empty element before insertion")
	void test7() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300, 400, 500, 600, 700 },
				new int[] { 5, 10, 15, 20, 25, 0, 35 });

		final int price = 150;
		final int volume = 7;
		container.set(price, volume);

		assertEquals(7, container.getSize());
		assertEquals(100, container.getPrice(0));
		assertEquals(5, container.getVolume(0));
		assertEquals(price, container.getPrice(1));
		assertEquals(volume, container.getVolume(1));
		assertEquals(200, container.getPrice(2));
		assertEquals(10, container.getVolume(2));
		assertEquals(300, container.getPrice(3));
		assertEquals(15, container.getVolume(3));
		assertEquals(400, container.getPrice(4));
		assertEquals(20, container.getVolume(4));
		assertEquals(500, container.getPrice(5));
		assertEquals(25, container.getVolume(5));
		assertEquals(700, container.getPrice(6));
		assertEquals(35, container.getVolume(6));
	}

	@Test
	@DisplayName("should contain three elements in correct order")
	void test8() {
		ArrayContainer container = new NaturalOrderContainer(1);
		final int price1 = 2000;
		final int volume1 = 200;
		container.set(price1, volume1);
		final int price2 = 1000;
		final int volume2 = 100;
		container.set(price2, volume2);
		final int price3 = 500;
		final int volume3 = 50;
		container.set(price3, volume3);

		assertEquals(3, container.getSize());
		assertEquals(price3, container.getPrice(0));
		assertEquals(volume3, container.getVolume(0));
		assertEquals(price2, container.getPrice(1));
		assertEquals(volume2, container.getVolume(1));
		assertEquals(price1, container.getPrice(2));
		assertEquals(volume1, container.getVolume(2));
	}

	@Test
	@DisplayName("should one element be created and its volume set to given value")
	void test9() {
		ArrayContainer container = new NaturalOrderContainer(1);
		final int price1 = 1000;
		final int volume1 = 100;
		final int volume2 = 1000;
		container.set(price1, volume1);
		container.set(price1, volume2);

		assertEquals(1, container.getSize());
		assertEquals(price1, container.getPrice(0));
		assertEquals(volume2, container.getVolume(0));
	}

	@Test
	@DisplayName("search via indexOf should continue until key is within left and right border")
	void test9a() {
		final int count = 1000;
		ArrayContainer container = new NaturalOrderContainer(count);
		int price = 1;
		for (int k = 0; k < count; k++) {
			container.set(price, 1);
			price += 2;
		}

		price = 2;
		for (int k = 0; k < count; k++) {
			container.set(price, 2);
			price += 2;
		}

		assertEquals(2 * count, container.getSize());

		price = 1;
		for (int k = 0; k < 2 * count; k++) {
			assertEquals(price, container.getPrice(k));
			assertEquals(k % 2 == 0 ? 1 : 2, container.getVolume(k));
			price += 1;
		}

	}

	@Test
	@DisplayName("check no value of best price for empty container")
	void test10() {
		ArrayContainer container = new NaturalOrderContainer(1);

		assertEquals(ArrayContainer.PRICE_VALUE_ABSENT, container.getBestPrice());
	}

	@Test
	@DisplayName("check value of best price for non empty container")
	void test11() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		assertEquals(300, container.getBestPrice());
	}

	@Test
	@DisplayName("should return volume by price if found")
	void test12() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		assertEquals(5, container.getVolumeByPrice(100));
		assertEquals(10, container.getVolumeByPrice(200));
		assertEquals(15, container.getVolumeByPrice(300));
	}

	@Test
	@DisplayName("should return absent volume value if price not found")
	void test13() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		assertEquals(ArrayContainer.VOLUME_VALUE_ABSENT, container.getVolumeByPrice(110));
		assertEquals(ArrayContainer.VOLUME_VALUE_ABSENT, container.getVolumeByPrice(220));
		assertEquals(ArrayContainer.VOLUME_VALUE_ABSENT, container.getVolumeByPrice(330));
	}

	@Test
	@DisplayName("should subtract given volume for best price if subtracted volume is less than element's volume")
	void test14() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		assertEquals(5, container.subtractVolumeForBestPrice(5));
		assertEquals(10, container.getVolumeByPrice(300));
		assertEquals(10, container.getVolumeByPrice(200));
		assertEquals(5, container.getVolumeByPrice(100));
		assertEquals(3, container.getSize());
	}

	@Test
	@DisplayName("should remove element for best price if volume is enough")
	void test15() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		assertEquals(15, container.subtractVolumeForBestPrice(15));
		assertEquals(10, container.getVolumeByPrice(200));
		assertEquals(5, container.getVolumeByPrice(100));
		assertEquals(2, container.getSize());
	}

	@Test
	@DisplayName("should remove last element for best price and decrease adjacent element")
	void test15a() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		assertEquals(20, container.subtractVolumeForBestPrice(20));
		assertEquals(5, container.getVolumeByPrice(200));
		assertEquals(5, container.getVolumeByPrice(100));
		assertEquals(2, container.getSize());
	}

	@Test
	@DisplayName("should remove two last elements and decrease first element")
	void test15b() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		assertEquals(26, container.subtractVolumeForBestPrice(26));
		assertEquals(4, container.getVolumeByPrice(100));
		assertEquals(1, container.getSize());
	}

	@Test
	@DisplayName("should remove adjacent empty elements and decrease non-empty elements")
	void test15c() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300, 400, 500 },
				new int[] { 5, 0, 0, 10, 15 });

		assertEquals(26, container.subtractVolumeForBestPrice(26));
		assertEquals(4, container.getVolumeByPrice(100));
		assertEquals(1, container.getSize());
	}

	@Test
	@DisplayName("should remove elements and return real decreased volume if given volume greater than presented")
	void test15d() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300, 400, 500 },
				new int[] { 5, 0, 0, 10, 15 });

		assertEquals(30, container.subtractVolumeForBestPrice(35));
		assertEquals(0, container.getSize());
	}

	@Test
	@DisplayName("should return price and volume for best price value in container")
	void test16() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300 }, new int[] { 5, 10, 15 });

		var priceVolume = container.getBestPriceValue();
		assertEquals(300, priceVolume.getPrice());
		assertEquals(15, priceVolume.getVolume());
	}

	@Test
	@DisplayName("should return null for best price value in empty container")
	void test17() {
		ArrayContainer container = new NaturalOrderContainer(1);

		var priceVolume = container.getBestPriceValue();
		assertNull(priceVolume);
	}

	@Test
	@DisplayName("should remove elements for best price if subtracted volume is same or greater and previous elements are empty")
	void test18() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300, 400, 500 },
				new int[] { 5, 0, 0, 0, 25 });

		assertEquals(5, container.getSize());
		assertEquals(25, container.subtractVolumeForBestPrice(25));
		assertEquals(1, container.getSize());
	}

	@Test
	@DisplayName("last element should be removed if size set to 0")
	void test19() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300, 400, 500 },
				new int[] { 5, 10, 15, 20, 25 });
		container.set(500, 0);

		assertEquals(4, container.getSize());
		assertEquals(500, container.getPrice(4));
		assertEquals(25, container.getVolume(4));
	}

	@Test
	@DisplayName("last element and preceding empty elements should be removed if size set to 0 for last element")
	void test20() {
		ArrayContainer container = new NaturalOrderContainer(new int[] { 100, 200, 300, 400, 500 },
				new int[] { 5, 0, 0, 0, 25 });
		container.set(500, 0);

		assertEquals(1, container.getSize());
		assertEquals(5, container.getVolume(0));
		assertEquals(100, container.getPrice(0));
	}

}
