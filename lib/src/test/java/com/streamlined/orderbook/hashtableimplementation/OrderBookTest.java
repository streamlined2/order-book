package com.streamlined.orderbook.hashtableimplementation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.streamlined.orderbook.OrderBook;

class OrderBookTest {

	@Test
	@DisplayName("query size should return 0 for empty book")
	void test1() {
		OrderBook book = new OrderBook(1);

		assertEquals(0, book.querySizeForPrice(1));
		assertEquals(0, book.querySizeForPrice(2));
		assertEquals(0, book.querySizeForPrice(3));

		assertEquals(Integer.MAX_VALUE, book.getMinAskPrice());
		assertEquals(0, book.getMaxBidPrice());

		assertNull(book.queryBestAsk());
		assertNull(book.queryBestBid());
	}

	@Test
	@DisplayName("query bid size should return previously set value")
	void test2() {
		OrderBook book = new OrderBook(1);

		book.setBidSize(100, 1000);
		book.setBidSize(200, 2000);
		book.setBidSize(300, 3000);

		assertEquals(1000, book.querySizeForPrice(100));
		assertEquals(2000, book.querySizeForPrice(200));
		assertEquals(3000, book.querySizeForPrice(300));

		assertEquals(300, book.getMaxBidPrice());

		assertEquals(300, book.queryBestBid().getPrice());
		assertEquals(3000, book.queryBestBid().getVolume());
	}

	@Test
	@DisplayName("query ask size should return previously set value")
	void test3() {
		OrderBook book = new OrderBook(1);

		book.setAskSize(100, 1000);
		book.setAskSize(200, 2000);
		book.setAskSize(300, 3000);

		assertEquals(1000, book.querySizeForPrice(100));
		assertEquals(2000, book.querySizeForPrice(200));
		assertEquals(3000, book.querySizeForPrice(300));
		assertEquals(100, book.getMinAskPrice());

		assertEquals(100, book.queryBestAsk().getPrice());
		assertEquals(1000, book.queryBestAsk().getVolume());
	}

	@Test
	@DisplayName("query bid/ask size should return previously set value")
	void test4() {
		OrderBook book = new OrderBook(1);

		book.setBidSize(100, 1000);
		book.setBidSize(200, 2000);
		book.setBidSize(300, 3000);

		book.setAskSize(400, 500);
		book.setAskSize(500, 1500);
		book.setAskSize(600, 2500);

		assertEquals(1000, book.querySizeForPrice(100));
		assertEquals(2000, book.querySizeForPrice(200));
		assertEquals(3000, book.querySizeForPrice(300));

		assertEquals(0, book.querySizeForPrice(350));

		assertEquals(500, book.querySizeForPrice(400));
		assertEquals(1500, book.querySizeForPrice(500));
		assertEquals(2500, book.querySizeForPrice(600));

		assertEquals(400, book.getMinAskPrice());
		assertEquals(300, book.getMaxBidPrice());

		assertEquals(400, book.queryBestAsk().getPrice());
		assertEquals(500, book.queryBestAsk().getVolume());
		assertEquals(300, book.queryBestBid().getPrice());
		assertEquals(3000, book.queryBestBid().getVolume());
	}

	@Test
	@DisplayName("buy ask/sell bid should decrease size")
	void test5() {
		OrderBook book = new OrderBook(1);

		book.setBidSize(100, 1000);
		book.setBidSize(200, 2000);
		book.setBidSize(300, 3000);

		book.setAskSize(400, 500);
		book.setAskSize(500, 1500);
		book.setAskSize(600, 2500);

		book.buyBestAsk(1000);
		book.sellBestBid(4000);

		assertEquals(500, book.getMinAskPrice());
		assertEquals(200, book.getMaxBidPrice());

		assertEquals(500, book.queryBestAsk().getPrice());
		assertEquals(1000, book.queryBestAsk().getVolume());

		assertEquals(200, book.queryBestBid().getPrice());
		assertEquals(1000, book.queryBestBid().getVolume());

		assertEquals(0, book.querySizeForPrice(50));
		assertEquals(1000, book.querySizeForPrice(100));
		assertEquals(0, book.querySizeForPrice(150));
		assertEquals(1000, book.querySizeForPrice(200));
		assertEquals(0, book.querySizeForPrice(250));
		assertEquals(0, book.querySizeForPrice(300));
		assertEquals(0, book.querySizeForPrice(350));
		assertEquals(0, book.querySizeForPrice(400));
		assertEquals(0, book.querySizeForPrice(450));
		assertEquals(1000, book.querySizeForPrice(500));
		assertEquals(0, book.querySizeForPrice(550));
		assertEquals(2500, book.querySizeForPrice(600));
		assertEquals(0, book.querySizeForPrice(650));
	}

}
