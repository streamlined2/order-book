package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.BestPriceVolumeSubtractResult;
import com.streamlined.orderbook.PriceVolume;
import com.streamlined.orderbook.hashtableimplementation.List.SubtractionResult;

public class AskContainer extends HashtableContainer {

	private static final boolean ASCENDING_FLAG = true;

	public AskContainer() {
		super();
	}

	public AskContainer(int capacity) {
		super(capacity);
	}

	public AskContainer(int capacity, ListPool listPool) {
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
			return getFirstNonEmptyNode(minPriceGroupIndex, maxPriceGroupIndex);
		} else {
			PriceVolume priceVolume = getFirstNonEmptyNode(minPriceGroupIndex, priceGroups.length - 1);
			if (priceVolume != null) {
				return priceVolume;
			}
			return getFirstNonEmptyNode(0, maxPriceGroupIndex);
		}
	}

	private PriceVolume getFirstNonEmptyNode(int startIndex, int lastIndex) {
		for (int index = startIndex; index <= lastIndex; index++) {
			if (priceGroups[index] != null) {
				PriceVolume priceVolume = priceGroups[index].getFirstNonEmptyNode();
				if (priceVolume != null) {
					return priceVolume;
				} else {
					listPool.add(priceGroups[index]);
					priceGroups[index] = null;
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
				lastCheckedPrice = result.lastCheckedOrder;
				if (priceGroups[index].isEmpty()) {
					listPool.add(priceGroups[index]);
					priceGroups[index] = null;
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
