package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.BestPriceVolumeSubtractResult;
import com.streamlined.orderbook.PriceVolume;
import com.streamlined.orderbook.hashtableimplementation.List.SubtractionResult;

public class BidContainer extends HashtableContainer {

	private static final boolean ASCENDING_FLAG = false;

	public BidContainer() {
		super();
	}

	public BidContainer(int capacity) {
		super(capacity);
	}

	public BidContainer(int capacity, ListPool listPool) {
		super(capacity, listPool);
	}

	@Override
	protected List createPriceGroupList(int price, int volume) {
		List list = listPool.get();
		if (list != null) {
			list.initialize(ASCENDING_FLAG);
			list.addLast(price, volume);
			return list;
		}
		return new OrderedArrayList(ASCENDING_FLAG, PRICE_GROUP_SIZE, price, volume);
	}

	@Override
	protected PriceVolume locateBestPriceVolume() {
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			return getFirstNonEmptyNode(maxPriceGroupIndex, minPriceGroupIndex);
		} else {
			PriceVolume priceVolume = getFirstNonEmptyNode(maxPriceGroupIndex, 0);
			if (priceVolume != null) {
				return priceVolume;
			}
			return getFirstNonEmptyNode(priceGroups.length - 1, minPriceGroupIndex);
		}
	}

	private PriceVolume getFirstNonEmptyNode(int startIndex, int lastIndex) {
		for (int index = startIndex; index >= lastIndex; index--) {
			if (priceGroups[index] != null) {
				PriceVolume priceVolume = priceGroups[index].getFirstNonEmptyNode();
				if (priceVolume != null) {
					return priceVolume;
				} else {
					listPool.add(priceGroups[index]);
					priceGroups[index] = null;
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
				lastCheckedPrice = result.lastCheckedOrder;
				if (priceGroups[index].isEmpty()) {
					listPool.add(priceGroups[index]);
					priceGroups[index] = null;
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
