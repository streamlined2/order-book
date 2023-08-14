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
		final int minPriceGroupIndex = getMinPriceGroupIndex();
		if (minPriceGroupIndex < getCapacity()) {
			return minPriceGroupIndex;
		}
		if (getMaxPriceGroupIndex() >= 0) {
			return 0;
		}
		return VALUE_UNDEFINED;
	}

}
