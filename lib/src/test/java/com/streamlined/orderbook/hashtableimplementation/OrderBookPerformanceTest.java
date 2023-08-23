package com.streamlined.orderbook.hashtableimplementation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.streamlined.orderbook.OrderBook;

class OrderBookPerformanceTest {

	@Test
	@DisplayName("measure search time for book")
	void testMeasureSearchTimeForBook() {
		// setup
		final int capacity = 10_000_000;
		final int minAskPrice = 2 * capacity;
		final int maxAskPrice = 3 * capacity;
		final int minBidPrice = 0;
		final int maxBidPrice = capacity;
		final int volume = 500;

		OrderBook book = new OrderBook(capacity);
		for (int price = minBidPrice; price <= maxBidPrice; price++) {
			book.setBidSize(price, volume);
		}
		for (int price = minAskPrice; price <= maxAskPrice; price++) {
			book.setAskSize(price, volume);
		}

		// measure
		long totalVolume = 0;
		long start = System.currentTimeMillis();
		for (int price = minBidPrice; price <= maxBidPrice; price++) {
			totalVolume += book.querySizeForPrice(price);
		}
		for (int price = minAskPrice; price <= maxAskPrice; price++) {
			totalVolume += book.querySizeForPrice(price);
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for search in book = %d, total = %d%n".formatted(time, totalVolume));
	}

	@Test
	@DisplayName("measure insertion time for book")
	void testMeasureInsertionTimeForBook() {
		// setup
		final int capacity = 10_000_000;
		final int minAskPrice = 2 * capacity;
		final int maxAskPrice = 3 * capacity;
		final int minBidPrice = 0;
		final int maxBidPrice = capacity;
		final int volume = 500;

		OrderBook book = new OrderBook(capacity);
		// measure
		long start = System.currentTimeMillis();
		for (int price = minBidPrice; price <= maxBidPrice; price++) {
			book.setBidSize(price, volume);
		}
		for (int price = minAskPrice; price <= maxAskPrice; price++) {
			book.setAskSize(price, volume);
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for insertion in book = %d%n".formatted(time));
	}

	@Test
	@DisplayName("measure volume subtraction for book")
	void testVolumeSubtractionForBestPrice() {
		// setup
		final int capacity = 10_000_000;
		final int minAskPrice = 2 * capacity;
		final int maxAskPrice = 3 * capacity;
		final int minBidPrice = 0;
		final int maxBidPrice = capacity;
		final int volume = 500;

		OrderBook book = new OrderBook(capacity);
		for (int price = minBidPrice; price <= maxBidPrice; price++) {
			book.setBidSize(price, volume);
		}
		for (int price = minAskPrice; price <= maxAskPrice; price++) {
			book.setAskSize(price, volume);
		}

		// measure
		final int buyout = 1000;
		final int times = 1_000_000;
		long start = System.currentTimeMillis();
		for (int k = 0; k < times; k++) {
			book.sellBestBid(buyout);
		}
		for (int k = 0; k < times; k++) {
			book.buyBestAsk(buyout);
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for buy/sell ask/bid = %d%n".formatted(time));
	}

	@Test
	@DisplayName("measure best price calculation after huge buyout for book")
	void testBestPriceCalculation() {
		// setup
		final int capacity = 10_000_000;
		final int minAskPrice = 2 * capacity;
		final int maxAskPrice = 3 * capacity;
		final int minBidPrice = 0;
		final int maxBidPrice = capacity;
		final int volume = 500;

		OrderBook book = new OrderBook(capacity);
		for (int price = minBidPrice; price <= maxBidPrice; price++) {
			book.setBidSize(price, volume);
		}
		for (int price = minAskPrice; price <= maxAskPrice; price++) {
			book.setAskSize(price, volume);
		}

		final int buyout = capacity * volume;
		book.sellBestBid(buyout);
		book.buyBestAsk(buyout);

		// measure
		final int times = 1_000_000;
		long start = System.currentTimeMillis();
		for (int k = 0; k < times; k++) {
			book.queryBestBid();
		}
		for (int k = 0; k < times; k++) {
			book.queryBestAsk();
		}
		long time = System.currentTimeMillis() - start;
		System.out.print("time for best price = %d%n".formatted(time));
	}

}
