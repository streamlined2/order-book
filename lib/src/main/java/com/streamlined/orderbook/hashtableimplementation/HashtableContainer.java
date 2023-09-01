package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.BestPriceVolumeSubtractResult;
import com.streamlined.orderbook.PriceVolume;
import com.streamlined.orderbook.VolumeContainer;

public abstract class HashtableContainer implements VolumeContainer {

	private static final int INITIAL_CAPACITY = 1000;
	private static final int LIST_POOL_DEFAULT_INITIAL_CAPACITY = 100;
	private static final int PRICE_GROUP_SIZE_POWER = 2;
	protected static final int PRICE_GROUP_SIZE = 1 << PRICE_GROUP_SIZE_POWER;

	protected static final int VALUE_UNDEFINED = -1;

	private static final int EXPANSION_NUMERATOR = 130;
	private static final int EXPANSION_DENOMINATOR = 100;
	private static final int EXPANSION_CONSTANT = 1;

	protected static final PriceVolume lastPriceVolume = new PriceVolume();
	protected static final VolumeSubtractResult volumeSubtractResult = new VolumeSubtractResult();
	protected static final BestPriceVolumeSubtractResult bestPriceVolumeSubtractResult = new BestPriceVolumeSubtractResult();

	protected final ListPool listPool;
	protected List[] priceGroups;
	protected int maxPriceGroupIndex;
	protected int minPriceGroupIndex;
	protected int firstPriceGroupStart;
	protected int firstPriceGroupIndex;

	protected HashtableContainer() {
		this(INITIAL_CAPACITY, new ListPool(LIST_POOL_DEFAULT_INITIAL_CAPACITY));
	}

	protected HashtableContainer(int capacity) {
		this(capacity, new ListPool(LIST_POOL_DEFAULT_INITIAL_CAPACITY));
	}

	protected HashtableContainer(int capacity, ListPool listPool) {
		this.listPool = listPool;
		priceGroups = new OrderedArrayList[capacity];
		firstPriceGroupStart = VALUE_UNDEFINED;
	}

	int locateGroupForPrice(int price) {
		defineFirstPriceGroupStart(price);
		int index = mapPriceToGroupIndex(price);
		if (index < 0) {
			expandContainer(-index);
			index = mapPriceToGroupIndex(price);
		}
		updateMinMaxIndices(price, index);
		return index;
	}

	int locateGroupForPriceNoExpansion(int price) {
		defineFirstPriceGroupStart(price);
		return mapPriceToGroupIndex(price);
	}

	void defineFirstPriceGroupStart(int firstPrice) {
		if (firstPriceGroupStart == VALUE_UNDEFINED) {
			firstPriceGroupStart = firstPrice >> PRICE_GROUP_SIZE_POWER << PRICE_GROUP_SIZE_POWER;
			firstPriceGroupIndex = maxPriceGroupIndex = minPriceGroupIndex = priceGroups.length >> 1;
		}
	}

	int mapPriceToGroupIndex(int price) {
		final int offset = getPriceGroupOffset(price);
		final int expansionSize = offset + 1 - priceGroups.length;
		if (expansionSize > 0) {
			return -expansionSize;
		}
		int index = offset + firstPriceGroupIndex;
		if (index < 0) {
			index += priceGroups.length;
		} else if (priceGroups.length <= index) {
			index -= priceGroups.length;
		}
		return index;
	}

	int getPriceGroupOffset(int price) {
		return (price - firstPriceGroupStart) >> PRICE_GROUP_SIZE_POWER;
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

	protected int getOccupiedSpace() {
		if (minPriceGroupIndex <= maxPriceGroupIndex) {
			return maxPriceGroupIndex - minPriceGroupIndex + 1;
		} else {
			return (maxPriceGroupIndex + 1) + (priceGroups.length - minPriceGroupIndex);
		}
	}

	public static int getPriceGroupSize() {
		return PRICE_GROUP_SIZE;
	}

	protected boolean isFull() {
		return getOccupiedSpace() == priceGroups.length;
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
		final List[] newPriceGroups = new OrderedArrayList[newCapacity];

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
			priceGroups[index] = createPriceGroupList(price, volume);
		} else {
			priceGroups[index].setAdd(price, volume);
		}
	}

	protected abstract List createPriceGroupList(int price, int volume);

	@Override
	public int getVolumeByPrice(int price) {
		final int index = locateGroupForPriceNoExpansion(price);
		if (priceGroups[index] == null) {
			return 0;
		}
		final PriceVolume priceVolume = priceGroups[index].getPriceVolumeByPrice(price);
		if (priceVolume == null) {
			return 0;
		}
		return priceVolume.getVolume();
	}

	@Override
	public int getBestPrice() {
		final PriceVolume priceVolume = locateBestPriceVolume();
		if (priceVolume == null) {
			return PRICE_VALUE_ABSENT;
		}
		return priceVolume.getPrice();
	}

	@Override
	public PriceVolume getBestPriceVolume() {
		final PriceVolume priceVolume = locateBestPriceVolume();
		if (priceVolume == null) {
			return null;
		}
		lastPriceVolume.setPriceVolume(priceVolume.getPrice(), priceVolume.getVolume());
		return lastPriceVolume;
	}

	protected abstract PriceVolume locateBestPriceVolume();

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

	protected static class VolumeSubtractResult {
		int lastCheckedPrice;
		int leftOver;

		void setValues(int lastCheckedPrice, int leftOver) {
			this.lastCheckedPrice = lastCheckedPrice;
			this.leftOver = leftOver;
		}
	}

}
