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
	protected Node locateBestPriceNode() {
		Node node = getFirstNonEmptyNode(maxPriceGroupIndex, 0);
		if (node != null) {
			return node;
		}
		return getFirstNonEmptyNode(priceGroups.length - 1, minPriceGroupIndex);
	}

	private Node getFirstNonEmptyNode(int startIndex, int lastIndex) {
		for (int index = startIndex; index >= lastIndex; index--) {
			if (priceGroups[index] != null) {
				Node node = priceGroups[index].getFirstNonEmptyNode();
				if (node != null) {
					return node;
				}
			}
		}
		return null;
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