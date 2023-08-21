package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.hashtableimplementation.OrderedLinkedList.Node;
import com.streamlined.orderbook.hashtableimplementation.OrderedLinkedList.SubtractionResult;

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
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			return getFirstNonEmptyNode(maxPriceGroupIndex, minPriceGroupIndex);
		} else {
			Node node = getFirstNonEmptyNode(maxPriceGroupIndex, 0);
			if (node != null) {
				return node;
			}
			return getFirstNonEmptyNode(priceGroups.length - 1, minPriceGroupIndex);
		}
	}

	private Node getFirstNonEmptyNode(int startIndex, int lastIndex) {
		for (int index = startIndex; index >= lastIndex; index--) {
			if (priceGroups[index] != null) {
				Node node = priceGroups[index].getFirstNonEmptyNode();
				if (node != null) {
					return node;
				} else {
					contractMaxSide();
				}
			}
		}
		return null;
	}

	@Override
	public int subtractVolumeForBestPrice(int subtractVolume) {
		int leftOver = subtractVolume;
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			leftOver = subtractVolumeForRange(maxPriceGroupIndex, minPriceGroupIndex, leftOver);
		} else {
			leftOver = subtractVolumeForRange(maxPriceGroupIndex, 0, leftOver);
			leftOver = subtractVolumeForRange(priceGroups.length - 1, minPriceGroupIndex, leftOver);
		}
		return subtractVolume - leftOver;
	}

	private int subtractVolumeForRange(int startIndex, int finishIndex, int leftOver) {
		for (int index = startIndex; leftOver > 0 && index >= finishIndex; index--) {
			if (priceGroups[index] != null) {
				SubtractionResult result = priceGroups[index].subtractVolume(leftOver);
				leftOver -= result.subtractedVolume;
				if (result.isListEmpty()) {
					contractMaxSide();
				}
			} else {
				contractMaxSide();
			}
		}
		return leftOver;
	}

}
