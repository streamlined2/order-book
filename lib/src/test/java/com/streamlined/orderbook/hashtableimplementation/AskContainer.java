package com.streamlined.orderbook.hashtableimplementation;

public class AskContainer extends HashtableContainer {

	public AskContainer() {
		super();
	}

	public AskContainer(int capacity) {
		super(capacity);
	}

	@Override
	protected OrderedLinkedList createPriceGroupList() {
		return new OrderedLinkedList(true);
	}

	@Override
	public int getBestPriceGroupIndex() {
		if (minPriceGroupIndex < priceGroups.length) {
			return minPriceGroupIndex;
		}
		if (maxPriceGroupIndex >= 0) {
			return 0;
		}
		return VALUE_UNDEFINED;
	}

	@Override
	public int subtractVolumeForBestPrice(int subtractVolume) {
		int leftOver = subtractVolume;
		for (int index = minPriceGroupIndex; index < priceGroups.length; index++) {
			if (priceGroups[index] != null) {
				leftOver -= priceGroups[index].subtractVolume(leftOver);
			}
		}
		for (int index = 0; index <= maxPriceGroupIndex; index++) {
			if (priceGroups[index] != null) {
				leftOver -= priceGroups[index].subtractVolume(leftOver);
			}
		}
		return subtractVolume - leftOver;
	}

}
