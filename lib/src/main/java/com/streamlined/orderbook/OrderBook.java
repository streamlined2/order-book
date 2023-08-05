package com.streamlined.orderbook;

public class OrderBook {

	private final VolumeContainer askContainer;
	private final VolumeContainer bidContainer;

	public OrderBook(int initialCapacity) {
		askContainer = new VolumeContainer(true, initialCapacity);
		bidContainer = new VolumeContainer(initialCapacity);
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
		if (bestBidPrice != VolumeContainer.PRICE_VALUE_ABSENT && price <= bestBidPrice) {
			int bidSize = bidContainer.getVolumeByPrice(price);
			if (bidSize != VolumeContainer.VOLUME_VALUE_ABSENT) {
				return bidSize;
			}
		}
		int bestAskPrice = askContainer.getBestPrice();
		if (bestAskPrice != VolumeContainer.PRICE_VALUE_ABSENT && price >= bestAskPrice) {
			int askSize = askContainer.getVolumeByPrice(price);
			if (askSize != VolumeContainer.VOLUME_VALUE_ABSENT) {
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
