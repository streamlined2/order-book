package com.streamlined.orderbook;

import com.streamlined.orderbook.hashtableimplementation.HashtableContainer;
import com.streamlined.orderbook.hashtableimplementation.ListPool;
import com.streamlined.orderbook.hashtableimplementation.AskContainer;
import com.streamlined.orderbook.hashtableimplementation.BidContainer;

public class OrderBook {

	public static final int LIST_POOL_INITIAL_CAPACITY = 100_000;
	
	private static final int INITIAL_CAPACITY = 1000;// internal usage for testing

	private final HashtableContainer askContainer;
	private final HashtableContainer bidContainer;
	private final ListPool listPool;
	private int maxBidPrice;
	private int minAskPrice;

	public OrderBook() {
		this(INITIAL_CAPACITY);
	}

	public OrderBook(int initialCapacity) {
		listPool = new ListPool(LIST_POOL_INITIAL_CAPACITY, HashtableContainer.getPriceGroupSize());
		askContainer = new AskContainer(initialCapacity, listPool);
		bidContainer = new BidContainer(initialCapacity, listPool);
		maxBidPrice = 0;
		minAskPrice = Integer.MAX_VALUE;
	}

	public void setBidSize(int price, int size) {
		bidContainer.set(price, size);
		if (maxBidPrice < price && size > 0) {
			maxBidPrice = price;
		}
	}

	public void setAskSize(int price, int size) {
		askContainer.set(price, size);
		if (minAskPrice > price && size > 0) {
			minAskPrice = price;
		}
	}

	public PriceVolume queryBestBid() {
		return bidContainer.getBestPriceVolume();
	}

	public PriceVolume queryBestAsk() {
		return askContainer.getBestPriceVolume();
	}

	public int querySizeForPrice(int price) {
		if (price <= maxBidPrice) {
			return bidContainer.getVolumeByPrice(price);
		} else if (price >= minAskPrice) {
			return askContainer.getVolumeByPrice(price);
		}
		return 0;
	}

	public void buyBestAsk(int volume) {
		BestPriceVolumeSubtractResult result = askContainer.subtractVolumeForBestPrice(volume);
		minAskPrice = result.lastCheckedPrice;
	}

	public void sellBestBid(int volume) {
		BestPriceVolumeSubtractResult result = bidContainer.subtractVolumeForBestPrice(volume);
		maxBidPrice = result.lastCheckedPrice;
	}

	@Override
	public String toString() {
		return String.format("asks = %s, bids = %s", askContainer.toString(), bidContainer.toString());
	}

	public int getMaxBidPrice() {
		return maxBidPrice;
	}

	public int getMinAskPrice() {
		return minAskPrice;
	}

}
