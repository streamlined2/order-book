package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.BestPriceVolumeSubtractResult;
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
	protected OrderedLinkedList createPriceGroupList(int price, int volume) {
		return new OrderedLinkedList(false, price, volume);
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
	public BestPriceVolumeSubtractResult subtractVolumeForBestPrice(int subtractVolume) {
		VolumeSubtractResult volumeSubtractResult;
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			volumeSubtractResult = subtractVolumeForRange(maxPriceGroupIndex, minPriceGroupIndex, subtractVolume);
		} else {
			volumeSubtractResult = subtractVolumeForRange(maxPriceGroupIndex, 0, subtractVolume);
			volumeSubtractResult = subtractVolumeForRange(priceGroups.length - 1, minPriceGroupIndex,
					volumeSubtractResult.leftOver);
		}
		bestPriceVolumeSubtractResult.setValues(volumeSubtractResult.lastCheckedPrice,
				subtractVolume - volumeSubtractResult.leftOver);
		return bestPriceVolumeSubtractResult;
	}

	private VolumeSubtractResult subtractVolumeForRange(int startIndex, int finishIndex, int leftOver) {
		int lastCheckedPrice = VALUE_UNDEFINED;
		for (int index = startIndex; leftOver > 0 && index >= finishIndex; index--) {
			if (priceGroups[index] != null) {
				SubtractionResult result = priceGroups[index].subtractVolume(leftOver);
				leftOver -= result.subtractedVolume;
				lastCheckedPrice = result.getLastCheckedPrice();
				if (result.isListEmpty()) {
					contractMaxSide();
				}
			} else {
				contractMaxSide();
			}
		}
		volumeSubtractResult.setValues(lastCheckedPrice, leftOver);
		return volumeSubtractResult;
	}

}
