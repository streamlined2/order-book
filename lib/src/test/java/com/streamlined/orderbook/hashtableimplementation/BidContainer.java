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
		final int maxPriceGroupIndex = getMaxPriceGroupIndex();
		if (maxPriceGroupIndex >= 0) {
			return maxPriceGroupIndex;
		}
		if (getMinPriceGroupIndex() < getCapacity()) {
			return getCapacity() - 1;
		}
		return VALUE_UNDEFINED;
	}

}
