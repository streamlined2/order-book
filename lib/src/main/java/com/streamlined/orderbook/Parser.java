package com.streamlined.orderbook;

import java.io.File;

public class Parser {

	private final File file;
	private final OrderBook orderBook;

	public Parser(File file) {
		this.file = file;
		orderBook = new OrderBook();
	}

	public OrderBook getOrderBook() {
		return orderBook;
	}

}
