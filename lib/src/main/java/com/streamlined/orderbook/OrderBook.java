package com.streamlined.orderbook;

public class OrderBook {

	private final VolumeContainer askContainer;
	private final VolumeContainer bidContainer;

	public OrderBook() {
		askContainer = new VolumeContainer(true);
		bidContainer = new VolumeContainer();
	}

}
