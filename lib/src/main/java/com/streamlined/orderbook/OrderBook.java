package com.streamlined.orderbook;

import com.streamlined.orderbook.arrayimplementation.ArrayContainer;
import com.streamlined.orderbook.arrayimplementation.NaturalOrderContainer;
import com.streamlined.orderbook.arrayimplementation.ReversedOrderContainer;

public class OrderBook {

	private final VolumeContainer askContainer;
	private final VolumeContainer bidContainer;

	public OrderBook(int initialCapacity) {
		askContainer = new ReversedOrderContainer(initialCapacity);
		bidContainer = new NaturalOrderContainer(initialCapacity);
	}

	public void setBidSize(int price, int size) {
		bidContainer.set(price, size);
	}

	public void setAskSize(int price, int size) {
		askContainer.set(price, size);
	}

	public PriceVolume queryBestBid() {
		return bidContainer.getBestPriceValue();
	}

	public PriceVolume queryBestAsk() {
		return askContainer.getBestPriceValue();
	}

	public int querySizeForPrice(int price) {
		int bestBidPrice = bidContainer.getBestPrice();
		if (bestBidPrice != ArrayContainer.PRICE_VALUE_ABSENT && price <= bestBidPrice) {
			int bidSize = bidContainer.getVolumeByPrice(price);
			if (bidSize != ArrayContainer.VOLUME_VALUE_ABSENT) {
				return bidSize;
			}
		}
		int bestAskPrice = askContainer.getBestPrice();
		if (bestAskPrice != ArrayContainer.PRICE_VALUE_ABSENT && price >= bestAskPrice) {
			int askSize = askContainer.getVolumeByPrice(price);
			if (askSize != ArrayContainer.VOLUME_VALUE_ABSENT) {
				return askSize;
			}
		}
		return 0;
	}

	public void buyBestAsk(int volume) {
		askContainer.subtractVolumeForBestPrice(volume);
	}

	public void sellBestBid(int volume) {
		bidContainer.subtractVolumeForBestPrice(volume);
	}

	@Override
	public String toString() {
		return String.format("asks = %s, bids = %s", askContainer.toString(), bidContainer.toString());
	}

}
