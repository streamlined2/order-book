package com.streamlined.orderbook;

import com.streamlined.orderbook.hashtableimplementation.HashtableContainer;
import com.streamlined.orderbook.hashtableimplementation.AskContainer;
import com.streamlined.orderbook.hashtableimplementation.BidContainer;

public class OrderBook {

	private final HashtableContainer askContainer;
	private final HashtableContainer bidContainer;

	public OrderBook(int initialCapacity) {
		askContainer = new AskContainer(initialCapacity);
		bidContainer = new BidContainer(initialCapacity);
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
		final int bestBidPrice = bidContainer.getBestPrice();
		if (bestBidPrice != VolumeContainer.PRICE_VALUE_ABSENT && price <= bestBidPrice) {
			final int bidSize = bidContainer.getVolumeByPrice(price);
			if (bidSize != VolumeContainer.VOLUME_VALUE_ABSENT) {
				return bidSize;
			}
		}
		final int askSize = askContainer.getVolumeByPrice(price);
		if (askSize != VolumeContainer.VOLUME_VALUE_ABSENT) {
			return askSize;
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
