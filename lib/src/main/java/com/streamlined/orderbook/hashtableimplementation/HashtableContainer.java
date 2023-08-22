package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.PriceVolume;
import com.streamlined.orderbook.VolumeContainer;
import com.streamlined.orderbook.hashtableimplementation.OrderedLinkedList.Node;

public abstract class HashtableContainer implements VolumeContainer {

	private static final int INITIAL_CAPACITY = 1000;
	private static final int PRICE_GROUP_SIZE_POWER = 2;
	protected static final int PRICE_GROUP_SIZE = 1 << PRICE_GROUP_SIZE_POWER;

	protected static final int VALUE_UNDEFINED = -1;

	private static final int EXPANSION_NUMERATOR = 130;
	private static final int EXPANSION_DENOMINATOR = 100;
	private static final int EXPANSION_CONSTANT = 1;

	private static final PriceVolume lastPriceVolume = new PriceVolume();

	protected OrderedLinkedList[] priceGroups;
	protected int maxPriceGroupIndex;
	protected int minPriceGroupIndex;
	protected int firstPriceGroupStart;
	protected int firstPriceGroupIndex;

	protected HashtableContainer() {
		this(INITIAL_CAPACITY);
	}

	protected HashtableContainer(int capacity) {
		priceGroups = new OrderedLinkedList[capacity];
		firstPriceGroupStart = VALUE_UNDEFINED;
	}

	int locateGroupForPrice(int price) {
		defineFirstPriceGroupStart(price);
		int index = mapPriceToGroupIndex(price);
		final int minExpansionSize = getMinExpansionSize(price, index);
		if (minExpansionSize > 0) {
			expandContainer(minExpansionSize);
			index = mapPriceToGroupIndex(price);
		}
		updateMinMaxIndices(price, index);
		return index;
	}

	void defineFirstPriceGroupStart(int firstPrice) {
		if (firstPriceGroupStart == VALUE_UNDEFINED) {
			firstPriceGroupStart = firstPrice >> PRICE_GROUP_SIZE_POWER << PRICE_GROUP_SIZE_POWER;
			final int middleIndex = priceGroups.length >> 1;
			firstPriceGroupIndex = middleIndex;
			maxPriceGroupIndex = middleIndex;
			minPriceGroupIndex = middleIndex;
		}
	}

	int mapPriceToGroupIndex(int price) {
		int index = ((price - firstPriceGroupStart) >> PRICE_GROUP_SIZE_POWER) + firstPriceGroupIndex;
		if (-priceGroups.length <= index && index < 0) {
			index += priceGroups.length;
		} else if (priceGroups.length <= index && index < priceGroups.length << 1) {
			index -= priceGroups.length;
		}
		return index;
	}

	private int getMinExpansionSize(int price, int index) {
		int initialExpansionSize = getIndexExcess(index);
		if (initialExpansionSize > 0) {
			return initialExpansionSize;
		}
		if (price <= firstPriceGroupStart - PRICE_GROUP_SIZE && index >= firstPriceGroupIndex) {
			return maxPriceGroupIndex - index + 1;
		} else if (price >= firstPriceGroupStart + PRICE_GROUP_SIZE && index <= firstPriceGroupIndex) {
			return index - minPriceGroupIndex + 1;
		}
		return 0;
	}

	private int getIndexExcess(int index) {
		int excess = 0;
		for (int indexRest = index; indexRest >= priceGroups.length; indexRest -= priceGroups.length) {
			excess += priceGroups.length;
		}
		return excess;
	}

	private void updateMinMaxIndices(int price, int index) {
		if ((price < firstPriceGroupStart)
				&& (index >= firstPriceGroupIndex && minPriceGroupIndex < firstPriceGroupIndex
						|| index < minPriceGroupIndex)) {
			minPriceGroupIndex = index;
		} else if ((price >= firstPriceGroupStart)
				&& (index < firstPriceGroupIndex && maxPriceGroupIndex >= firstPriceGroupIndex
						|| index > maxPriceGroupIndex)) {
			maxPriceGroupIndex = index;
		}
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

		if (minPriceGroupIndex <= maxPriceGroupIndex) {// one segment [minPriceGroupIndex..maxPriceGroupIndex]
			final int count = maxPriceGroupIndex - minPriceGroupIndex + 1;
			System.arraycopy(priceGroups, minPriceGroupIndex, newPriceGroups, minPriceGroupIndex, count);
		} else {// two distinct segments
				// [0..maxPriceGroupIndex],[minPriceGroupIndex..priceGroups.length-1]
			final int minPriceSegmentLength = priceGroups.length - minPriceGroupIndex;
			final int newMinPriceGroupIndex = 0;
			System.arraycopy(priceGroups, minPriceGroupIndex, newPriceGroups, newMinPriceGroupIndex,
					minPriceSegmentLength);

			final int maxPriceSegmentLength = maxPriceGroupIndex + 1;
			final int maxPriceSegmentStartIndex = newMinPriceGroupIndex + minPriceSegmentLength;
			System.arraycopy(priceGroups, 0, newPriceGroups, maxPriceSegmentStartIndex, maxPriceSegmentLength);

			if (0 <= firstPriceGroupIndex && firstPriceGroupIndex <= maxPriceGroupIndex) {
				firstPriceGroupIndex += maxPriceSegmentStartIndex;
			} else {
				firstPriceGroupIndex += newMinPriceGroupIndex - minPriceGroupIndex;
			}
			minPriceGroupIndex = newMinPriceGroupIndex;
			maxPriceGroupIndex = newMinPriceGroupIndex + minPriceSegmentLength + maxPriceSegmentLength - 1;
		}

		priceGroups = newPriceGroups;
	}

	@Override
	public int getSize() {
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			return getSizeForSegment(minPriceGroupIndex, maxPriceGroupIndex);
		} else {
			return getSizeForSegment(0, maxPriceGroupIndex)
					+ getSizeForSegment(minPriceGroupIndex, priceGroups.length - 1);
		}
	}

	private int getSizeForSegment(int startIndex, int finishIndex) {
		int size = 0;
		for (int k = startIndex; k <= finishIndex; k++) {
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
		priceGroups[index].setAdd(price, volume);
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
		final Node node = locateBestPriceNode();
		if (node == null) {
			return null;
		}
		lastPriceVolume.setPriceVolume(node.getOrder(), node.getVolume());
		return lastPriceVolume;
	}

	protected abstract Node locateBestPriceNode();

	protected final void contractMinSide() {
		if (minPriceGroupIndex != maxPriceGroupIndex) {
			minPriceGroupIndex++;
			if (minPriceGroupIndex >= priceGroups.length) {
				minPriceGroupIndex -= priceGroups.length;
			}
		}
	}

	protected final void contractMaxSide() {
		if (minPriceGroupIndex != maxPriceGroupIndex) {
			maxPriceGroupIndex--;
			if (maxPriceGroupIndex < 0) {
				maxPriceGroupIndex += priceGroups.length;
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("[");
		int count;
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			count = addStringForSegment(b, minPriceGroupIndex, maxPriceGroupIndex, 0);
		} else {
			count = addStringForSegment(b, 0, maxPriceGroupIndex, 0);
			count = addStringForSegment(b, minPriceGroupIndex, priceGroups.length - 1, count);
		}
		if (count > 0) {
			b.deleteCharAt(b.length() - 1);
		}
		b.append("]");
		return b.toString();
	}

	private int addStringForSegment(StringBuilder b, int startIndex, int finishIndex, int count) {
		for (int k = startIndex; k <= finishIndex; k++) {
			if (priceGroups[k] != null) {
				b.append(priceGroups[k].toString()).append(",");
				count++;
			}
		}
		return count;
	}

}
