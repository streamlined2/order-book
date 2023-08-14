package com.streamlined.orderbook.hashtableimplementation;

public class BidContainer extends HashtableContainer {

	public BidContainer() {
		super();
	}

	public BidContainer(int capacity) {
		super(capacity);
	}

	@Override
	protected OrderedLinkedList createPriceGroupList() {
		return new OrderedLinkedList(false);
	}

	@Override
	public int getBestPriceGroupIndex() {
		if (maxPriceGroupIndex >= 0) {
			return maxPriceGroupIndex;
		}
		if (minPriceGroupIndex < priceGroups.length) {
			return priceGroups.length - 1;
		}
		return VALUE_UNDEFINED;
	}

	@Override
	public int subtractVolumeForBestPrice(int subtractVolume) {
		int leftOver = subtractVolume;
		for (int index = maxPriceGroupIndex; index >= 0; index--) {
			if (priceGroups[index] != null) {
				leftOver -= priceGroups[index].subtractVolume(leftOver);
			}
		}
		for (int index = priceGroups.length - 1; index >= minPriceGroupIndex; index--) {
			if (priceGroups[index] != null) {
				leftOver -= priceGroups[index].subtractVolume(leftOver);
			}
		}
		return subtractVolume - leftOver;
	}

}
