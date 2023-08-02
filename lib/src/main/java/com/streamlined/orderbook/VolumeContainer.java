package com.streamlined.orderbook;

import java.util.Arrays;

public class VolumeContainer {

	private static final int INITIAL_CAPACITY = 1000;
	private static final int EXPANSION_NUMERATOR = 3;
	private static final int EXPANSION_DENOMINATOR = 2;

	private static final PriceVolume lastPriceVolume = new PriceVolume();
	public static final int VOLUME_VALUE_ABSENT = -1;

	private final boolean reversed;
	private int[] prices;
	private int[] volumes;
	private int size;

	public VolumeContainer() {
		this(false, INITIAL_CAPACITY);
	}

	public VolumeContainer(boolean reversed) {
		this(reversed, INITIAL_CAPACITY);
	}

	public VolumeContainer(int initialCapacity) {
		this(false, initialCapacity);
	}

	public VolumeContainer(boolean reversed, int initialCapacity) {
		this.reversed = reversed;
		final int capacity = initialCapacity > 0 ? initialCapacity : 1;
		prices = new int[capacity];
		volumes = new int[capacity];
	}

	public VolumeContainer(int[] prices, int[] volumes) {
		this(false, prices, volumes);
	}

	public VolumeContainer(boolean reversed, int[] prices, int[] volumes) {
		this(reversed, Math.min(prices.length, volumes.length));
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
		if (index == size - 1 && volume == 0) {
			removeEmptyVolumeItems(index);
		} else if (index >= 0) {
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
		System.arraycopy(prices, insertIndex, prices, insertIndex + 1, size - insertIndex);
		System.arraycopy(volumes, insertIndex, volumes, insertIndex + 1, size - insertIndex);
	}

	private void expand() {
		int newCapacity = prices.length * EXPANSION_NUMERATOR / EXPANSION_DENOMINATOR;
		newCapacity = newCapacity > prices.length ? newCapacity : prices.length + 1;

		final int[] newPrices = new int[newCapacity];
		System.arraycopy(prices, 0, newPrices, 0, prices.length);
		prices = newPrices;

		final int[] newVolumes = new int[newCapacity];
		System.arraycopy(volumes, 0, newVolumes, 0, volumes.length);
		volumes = newVolumes;
	}

	int indexOf(int price) {
		int leftIndex = 0;
		int rightIndex = size - 1;
		while (leftIndex <= rightIndex) {
			final int middleIndex = (leftIndex + rightIndex) / 2;
			final int middlePrice = prices[middleIndex];
			if (reversed && middlePrice > price || !reversed && middlePrice < price) {
				leftIndex = middleIndex + 1;
			} else if (reversed && middlePrice < price || !reversed && middlePrice > price) {
				rightIndex = middleIndex - 1;
			} else {
				return middleIndex;
			}
		}
		return -leftIndex - 1;
	}

	int getPrice(int index) {
		return prices[index];
	}

	int getVolume(int index) {
		return volumes[index];
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
		return null;// violates clear code convention
	}

	private boolean isIndexValid(int index) {
		return 0 <= index && index < size;
	}

	public int getVolumeByPrice(int price) {
		final int index = indexOf(price);
		if (isIndexValid(index)) {
			return volumes[index];
		}
		return VOLUME_VALUE_ABSENT;
	}

	public int subtractVolumeForBestPrice(int subtractVolume) {
		final int index = getBestPriceIndex();
		if (isIndexValid(index)) {
			final int previousVolume = volumes[index];
			if (previousVolume <= subtractVolume) {
				removeEmptyVolumeItems(index);
			} else {
				volumes[index] -= subtractVolume;
			}
			return previousVolume;
		}
		return VOLUME_VALUE_ABSENT;
	}

	private void removeEmptyVolumeItems(final int startIndex) {
		size--;
		for (int k = startIndex - 1; k >= 0 && volumes[k] == 0; k--) {
			size--;
		}
	}

	int getBestPriceIndex() {
		return size - 1;
	}

	int getBestPriceVolume() {
		return volumes[getBestPriceIndex()];
	}

}
