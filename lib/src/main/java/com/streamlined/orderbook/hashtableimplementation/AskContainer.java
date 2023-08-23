package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.hashtableimplementation.OrderedLinkedList.Node;
import com.streamlined.orderbook.hashtableimplementation.OrderedLinkedList.SubtractionResult;

public class AskContainer extends HashtableContainer {

	public AskContainer() {
		super();
	}

	public AskContainer(int capacity) {
		super(capacity);
	}

	@Override
	protected OrderedLinkedList createPriceGroupList(int price, int volume) {
		return new OrderedLinkedList(true, price, volume);
	}

	@Override
	protected Node locateBestPriceNode() {
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			return getFirstNonEmptyNode(minPriceGroupIndex, maxPriceGroupIndex);
		} else {
			Node node = getFirstNonEmptyNode(minPriceGroupIndex, priceGroups.length - 1);
			if (node != null) {
				return node;
			}
			return getFirstNonEmptyNode(0, maxPriceGroupIndex);
		}
	}

	private Node getFirstNonEmptyNode(int startIndex, int lastIndex) {
		for (int index = startIndex; index <= lastIndex; index++) {
			if (priceGroups[index] != null) {
				Node node = priceGroups[index].getFirstNonEmptyNode();
				if (node != null) {
					return node;
				} else {
					contractMinSide();
				}
			}
		}
		return null;
	}

	@Override
	public int subtractVolumeForBestPrice(int subtractVolume) {
		int leftOver = subtractVolume;
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			leftOver = subtractVolumeForRange(minPriceGroupIndex, maxPriceGroupIndex, leftOver);
		} else {
			leftOver = subtractVolumeForRange(minPriceGroupIndex, priceGroups.length - 1, leftOver);
			leftOver = subtractVolumeForRange(0, maxPriceGroupIndex, leftOver);
		}
		return subtractVolume - leftOver;
	}

	private int subtractVolumeForRange(int startIndex, int finishIndex, int leftOver) {
		for (int index = startIndex; leftOver > 0 && index <= finishIndex; index++) {
			if (priceGroups[index] != null) {
				SubtractionResult result = priceGroups[index].subtractVolume(leftOver);
				leftOver -= result.subtractedVolume;
				if (result.isListEmpty()) {
					contractMinSide();
				}
			} else {
				contractMinSide();
			}
		}
		return leftOver;
	}

}
