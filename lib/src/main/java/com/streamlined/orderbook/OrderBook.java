package com.streamlined.orderbook;

import com.streamlined.orderbook.hashtableimplementation.HashtableContainer;

import com.streamlined.orderbook.hashtableimplementation.AskContainer;
import com.streamlined.orderbook.hashtableimplementation.BidContainer;

public class OrderBook {

	private final HashtableContainer askContainer;
	private final HashtableContainer bidContainer;
	private int maxBidPrice;
	private int minAskPrice;

	public OrderBook(int initialCapacity) {
		askContainer = new AskContainer(initialCapacity);
		bidContainer = new BidContainer(initialCapacity);
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
		return bidContainer.getBestPriceValue();
	}

	public PriceVolume queryBestAsk() {
		return askContainer.getBestPriceValue();
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
