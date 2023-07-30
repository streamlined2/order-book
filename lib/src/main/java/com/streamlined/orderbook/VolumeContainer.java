package com.streamlined.orderbook;

import java.util.Arrays;

public class VolumeContainer {

	private static final int INITIAL_CAPACITY = 1000;
	private static final int EXPANSION_NUMERATOR = 3;
	private static final int EXPANSION_DENOMINATOR = 2;

	private static final PriceVolume lastPriceVolume = new PriceVolume();
	static final int VOLUME_VALUE_ABSENT = -1;

	private int[] prices;
	private int[] volumes;
	private int size;
	private boolean reversed;

	public VolumeContainer() {
		this(INITIAL_CAPACITY);
	}

	public VolumeContainer(int initialCapacity) {
		final int capacity = initialCapacity > 0 ? initialCapacity : 1;
		prices = new int[capacity];
		volumes = new int[capacity];
	}

	public VolumeContainer(int[] prices, int[] volumes) {
		this(Math.min(prices.length, volumes.length));
		for (int k = 0; k < this.prices.length; k++) {
			add(prices[k], volumes[k]);
		}
	}

	int getCapacity() {
		return prices.length;
	}

	public int getSize() {
		return size;
	}

	private int mapIndex(int index) {
		return reversed ? prices.length - size + index : index;
	}

	public void add(int price, int volume) {
		final int index = indexOf(price);
		if (index >= 0) {
			volumes[index] += volume;
		} else {
			freeCellAndStorePriceVolume(index, price, volume);
		}
	}

	public void set(int price, int volume) {
		final int index = indexOf(price);
		if (index >= 0) {
			volumes[index] = volume;
		} else {
			freeCellAndStorePriceVolume(index, price, volume);
		}
	}

	private void freeCellAndStorePriceVolume(final int index, int price, int volume) {
		if (size == prices.length) {
			expand();
		}
		final int insertIndex = -index - 1;
		freeCellByShifting(insertIndex);
		prices[insertIndex] = price;
		volumes[insertIndex] = volume;
		size++;
	}

	private void freeCellByShifting(int insertIndex) {
		if (reversed) {
			final int startIndex = mapIndex(0);
			System.arraycopy(prices, insertIndex, prices, insertIndex - 1, insertIndex - startIndex + 1);
			System.arraycopy(volumes, insertIndex, volumes, insertIndex - 1, insertIndex - startIndex + 1);
		} else {
			System.arraycopy(prices, insertIndex, prices, insertIndex + 1, size - insertIndex);
			System.arraycopy(volumes, insertIndex, volumes, insertIndex + 1, size - insertIndex);
		}
	}

	private void expand() {
		int newCapacity = prices.length * EXPANSION_NUMERATOR / EXPANSION_DENOMINATOR;
		newCapacity = newCapacity > prices.length ? newCapacity : newCapacity + 1;

		final int[] newPrices = new int[newCapacity];
		System.arraycopy(prices, 0, newPrices, 0, prices.length);
		prices = newPrices;

		final int[] newVolumes = new int[newCapacity];
		System.arraycopy(volumes, 0, newVolumes, 0, volumes.length);
		volumes = newVolumes;
	}

	int indexOf(int price) {
		int leftIndex = mapIndex(0);
		int rightIndex = mapIndex(size - 1);
		while (leftIndex <= rightIndex) {
			int middleIndex = (leftIndex + rightIndex) / 2;
			int middlePrice = prices[mapIndex(middleIndex)];
			if (middlePrice < price) {
				leftIndex = middleIndex + 1;
			} else if (middlePrice > price) {
				rightIndex = middleIndex - 1;
			} else {
				return middleIndex;
			}
		}
		return -leftIndex - 1;
	}

	int getPrice(int index) {
		return prices[mapIndex(index)];
	}

	int getVolume(int index) {
		return volumes[mapIndex(index)];
	}

	@Override
	public String toString() {
		return new StringBuilder().append("prices: ").append(Arrays.toString(prices)).append(", volumes: ")
				.append(Arrays.toString(volumes)).toString();
	}

	public PriceVolume getBestPriceValue() {
		final int index = getBestPriceIndex();
		if (isIndexValid(index)) {
			lastPriceVolume.setPriceVolume(prices[index], volumes[index]);
			return lastPriceVolume;
		}
		return null;
	}

	private boolean isIndexValid(int index) {
		return 0 <= index && index < prices.length;
	}

	public int getVolumeByPrice(int price) {
		final int index = indexOf(price);
		if (index >= 0) {
			return volumes[index];
		}
		return VOLUME_VALUE_ABSENT;
	}

	public int subtractVolumeForBestPrice(int subtractVolume) {
		final int index = getBestPriceIndex();
		final int previousVolume = volumes[index];
		if (previousVolume < subtractVolume) {
			size--;
		} else {
			volumes[index] -= subtractVolume;
		}
		return previousVolume;
	}

	int getBestPriceIndex() {
		if (reversed) {
			return mapIndex(0);
		} else {
			return mapIndex(size - 1);
		}
	}

	int getBestPriceVolume() {
		return volumes[getBestPriceIndex()];
	}

}
