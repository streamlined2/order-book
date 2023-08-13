package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.PriceVolume;
import com.streamlined.orderbook.VolumeContainer;

public class HashtableContainer implements VolumeContainer {

	private static final int INITIAL_CAPACITY = 1000;
	static final int VALUE_UNDEFINED = -1;

	static final int PRICE_GROUP_SIZE_POWER = 2;
	static final int PRICE_GROUP_SIZE = 1 << PRICE_GROUP_SIZE_POWER;

	private static final int EXPANSION_NUMERATOR = 130;
	private static final int EXPANSION_DENOMINATOR = 100;
	private static final int EXPANSION_CONSTANT = 1;

	private OrderedLinkedList[] priceGroups;
	private int firstPriceGroupStart;
	private int maxPriceGroupIndex;
	private int minPriceGroupIndex;

	public HashtableContainer() {
		this(INITIAL_CAPACITY);
	}

	public HashtableContainer(int capacity) {
		priceGroups = new OrderedLinkedList[capacity];
		firstPriceGroupStart = VALUE_UNDEFINED;
		maxPriceGroupIndex = -1;
		minPriceGroupIndex = priceGroups.length;
	}

	int defineFirstPriceGroupStart(int firstPrice, int groupSizePower) {
		if (firstPriceGroupStart == VALUE_UNDEFINED) {
			firstPriceGroupStart = firstPrice >> groupSizePower << groupSizePower;
		}
		return firstPriceGroupStart;
	}

	int mapPriceToGroupIndex(int price, int groupSizePower) {
		defineFirstPriceGroupStart(price, groupSizePower);
		int index = (price - firstPriceGroupStart) >> groupSizePower;
		if (index < 0) {
			index += priceGroups.length;
		}
		return index;
	}

	int getMinExpansionSize(int price, int index) {
		int minExpansionSize;
		if (price < firstPriceGroupStart) {
			minExpansionSize = maxPriceGroupIndex - index + 1;
		} else {
			minExpansionSize = index - minPriceGroupIndex + 1;
		}
		return minExpansionSize;
	}

	boolean needsExpansion(int price, int groupSizePower) {
		return getMinExpansionSize(price, mapPriceToGroupIndex(price, groupSizePower)) > 0;
	}

	void allocateCell(int price, int groupSizePower) {
		int index = mapPriceToGroupIndex(price, groupSizePower);
		int minExpansionSize = getMinExpansionSize(price, index);
		if (minExpansionSize > 0) {
			expandContainer(minExpansionSize);
		}
		if (price < firstPriceGroupStart) {
			minPriceGroupIndex = minPriceGroupIndex > index ? index : minPriceGroupIndex;
		} else {
			maxPriceGroupIndex = maxPriceGroupIndex < index ? index : maxPriceGroupIndex;
		}
	}

	private void expandContainer(int minExpansionSize) {
		int newCapacity = Math.max(priceGroups.length * EXPANSION_NUMERATOR / EXPANSION_DENOMINATOR,
				priceGroups.length + Math.max(minExpansionSize, EXPANSION_CONSTANT));
		final OrderedLinkedList[] newPriceGroups = new OrderedLinkedList[newCapacity];

		final int maxPriceSegmentLength = maxPriceGroupIndex + 1;
		System.arraycopy(priceGroups, 0, newPriceGroups, 0, maxPriceSegmentLength);

		final int minPriceSegmentLength = priceGroups.length - minPriceGroupIndex;
		final int newMinPriceGroupIndex = newPriceGroups.length - minPriceSegmentLength;
		System.arraycopy(priceGroups, minPriceGroupIndex, newPriceGroups, newMinPriceGroupIndex, minPriceSegmentLength);
		minPriceGroupIndex = newMinPriceGroupIndex;

		priceGroups = newPriceGroups;
	}

	@Override
	public int getSize() {
		int size = 0;
		for (int k = 0; k <= maxPriceGroupIndex; k++) {
			size += priceGroups[k].getSize();
		}
		for (int k = minPriceGroupIndex; k < priceGroups.length; k++) {
			size += priceGroups[k].getSize();
		}
		return size;
	}

	public boolean isEmpty() {
		return getSize() == 0;
	}

	@Override
	public void set(int price, int volume) {
		// TODO Auto-generated method stub

	}

	@Override
	public PriceVolume getBestPriceValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBestPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVolumeByPrice(int price) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int subtractVolumeForBestPrice(int subtractVolume) {
		// TODO Auto-generated method stub
		return 0;
	}

}
