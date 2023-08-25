package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.BestPriceVolumeSubtractResult;
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
	public BestPriceVolumeSubtractResult subtractVolumeForBestPrice(int subtractVolume) {
		VolumeSubtractResult volumeSubtractResult;
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			volumeSubtractResult = subtractVolumeForRange(minPriceGroupIndex, maxPriceGroupIndex, subtractVolume);
		} else {
			volumeSubtractResult = subtractVolumeForRange(minPriceGroupIndex, priceGroups.length - 1, subtractVolume);
			volumeSubtractResult = subtractVolumeForRange(0, maxPriceGroupIndex, volumeSubtractResult.leftOver);
		}
		bestPriceVolumeSubtractResult.setValues(volumeSubtractResult.lastCheckedPrice,
				subtractVolume - volumeSubtractResult.leftOver);
		return bestPriceVolumeSubtractResult;
	}

	private VolumeSubtractResult subtractVolumeForRange(int startIndex, int finishIndex, int leftOver) {
		int lastCheckedPrice = VALUE_UNDEFINED;
		for (int index = startIndex; leftOver > 0 && index <= finishIndex; index++) {
			if (priceGroups[index] != null) {
				SubtractionResult result = priceGroups[index].subtractVolume(leftOver);
				leftOver -= result.subtractedVolume;
				lastCheckedPrice = result.getLastCheckedPrice();
				if (result.isListEmpty()) {
					contractMinSide();
				}
			} else {
				contractMinSide();
			}
		}
		volumeSubtractResult.setValues(lastCheckedPrice, leftOver);
		return volumeSubtractResult;
	}

}
