package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.PriceVolume;
import com.streamlined.orderbook.VolumeContainer;

public abstract class HashtableContainer implements VolumeContainer {

	private static final int INITIAL_CAPACITY = 1000;
	private static final int PRICE_GROUP_SIZE_POWER = 2;

	public static final int VOLUME_VALUE_ABSENT = -1;
	public static final int PRICE_VALUE_ABSENT = -1;
	protected static final int VALUE_UNDEFINED = -1;

	private static final int EXPANSION_NUMERATOR = 130;
	private static final int EXPANSION_DENOMINATOR = 100;
	private static final int EXPANSION_CONSTANT = 1;

	private static final PriceVolume lastPriceVolume = new PriceVolume();

	protected OrderedLinkedList[] priceGroups;
	protected int firstPriceGroupStart;
	protected int maxPriceGroupIndex;
	protected int minPriceGroupIndex;

	protected HashtableContainer() {
		this(INITIAL_CAPACITY);
	}

	protected HashtableContainer(int capacity) {
		priceGroups = new OrderedLinkedList[capacity];
		firstPriceGroupStart = VALUE_UNDEFINED;
		maxPriceGroupIndex = -1;
		minPriceGroupIndex = priceGroups.length;
	}

	int defineFirstPriceGroupStart(int firstPrice) {
		if (firstPriceGroupStart == VALUE_UNDEFINED) {
			firstPriceGroupStart = firstPrice >> PRICE_GROUP_SIZE_POWER << PRICE_GROUP_SIZE_POWER;
		}
		return firstPriceGroupStart;
	}

	int mapPriceToGroupIndex(int price) {
		defineFirstPriceGroupStart(price);
		int index = (price - firstPriceGroupStart) >> PRICE_GROUP_SIZE_POWER;
		if (index < 0) {
			index += priceGroups.length;
		}
		return index;
	}

	private int getMinExpansionSize(int price, int index) {
		return belongsToMinPriceSegment(price) ? maxPriceGroupIndex - index + 1 : index - minPriceGroupIndex + 1;
	}

	private boolean belongsToMinPriceSegment(int price) {
		return price < firstPriceGroupStart;
	}

	int locateGroupForPrice(int price) {
		int index = mapPriceToGroupIndex(price);
		final int minExpansionSize = getMinExpansionSize(price, index);
		if (minExpansionSize > 0) {
			expandContainer(minExpansionSize);
			index = mapPriceToGroupIndex(price);
		}
		if (belongsToMinPriceSegment(price)) {
			minPriceGroupIndex = Math.min(index, minPriceGroupIndex);
		} else {
			maxPriceGroupIndex = Math.max(index, maxPriceGroupIndex);
		}
		return index;
	}

	int getMinPriceGroupIndex() {
		return minPriceGroupIndex;
	}

	int getMaxPriceGroupIndex() {
		return maxPriceGroupIndex;
	}

	int getCapacity() {
		return priceGroups.length;
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
			if (priceGroups[k] != null) {
				size += priceGroups[k].getSize();
			}
		}
		for (int k = minPriceGroupIndex; k < priceGroups.length; k++) {
			if (priceGroups[k] != null) {
				size += priceGroups[k].getSize();
			}
		}
		return size;
	}

	public boolean isEmpty() {
		return getSize() == 0;
	}

	@Override
	public void set(int price, int volume) {
		final int index = locateGroupForPrice(price);
		if (priceGroups[index] == null) {
			priceGroups[index] = createPriceGroupList();
		}
		priceGroups[index].add(price, volume);
	}

	protected abstract OrderedLinkedList createPriceGroupList();

	@Override
	public int getVolumeByPrice(int price) {
		final int index = locateGroupForPrice(price);
		if (priceGroups[index] == null) {
			return VOLUME_VALUE_ABSENT;
		}
		final Node node = priceGroups[index].getNodeByOrder(price);
		if (node == null) {
			return VOLUME_VALUE_ABSENT;
		}
		return node.getVolume();
	}

	@Override
	public int getBestPrice() {
		PriceVolume priceVolume = getBestPriceValue();
		if (priceVolume == null) {
			return PRICE_VALUE_ABSENT;
		}
		return priceVolume.getPrice();
	}

	@Override
	public PriceVolume getBestPriceValue() {
		final int bestPriceGroupIndex = getBestPriceGroupIndex();
		if (bestPriceGroupIndex == VALUE_UNDEFINED) {
			return null;
		}
		final OrderedLinkedList list = priceGroups[bestPriceGroupIndex];
		if (list == null) {
			return null;
		}
		final Node node = list.getFirstNonEmptyNode();
		if (node == null) {
			return null;
		}
		lastPriceVolume.setPriceVolume(node.getOrder(), node.getVolume());
		return lastPriceVolume;
	}

	protected abstract int getBestPriceGroupIndex();

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("[");
		int count = 0;
		for (int k = minPriceGroupIndex; k < priceGroups.length; k++) {
			if (priceGroups[k] != null) {
				b.append(priceGroups[k].toString()).append(",");
				count++;
			}
		}
		for (int k = 0; k <= maxPriceGroupIndex; k++) {
			if (priceGroups[k] != null) {
				b.append(priceGroups[k].toString()).append(",");
				count++;
			}
		}
		if (count > 0) {
			b.deleteCharAt(b.length() - 1);
		}
		b.append("]");
		return b.toString();
	}

}
