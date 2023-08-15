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
	protected Node locateBestPriceNode() {
		Node node = getFirstNonEmptyNode(minPriceGroupIndex, priceGroups.length - 1);
		if (node != null) {
			return node;
		}
		return getFirstNonEmptyNode(0, maxPriceGroupIndex);
	}

	private Node getFirstNonEmptyNode(int startIndex, int lastIndex) {
		for (int index = startIndex; index <= lastIndex; index++) {
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
