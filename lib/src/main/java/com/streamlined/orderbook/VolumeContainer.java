package com.streamlined.orderbook;

import java.util.Arrays;

public class VolumeContainer {

	private static final int INITIAL_CAPACITY = 1000;
	private static final int EXPANSION_NUMERATOR = 5;
	private static final int EXPANSION_DENOMINATOR = 2;

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

	public int getCapacity() {
		return prices.length;
	}

	public int getSize() {
		return size;
	}

	private int mapIndex(int index) {
		return reversed ? prices.length - 1 - index : index;
	}

	public void add(int price, int volume) {
		final int index = indexOf(price);
		if (index >= 0) {
			volumes[index] += volume;
		} else {
			if (size == prices.length) {
				expand();
			}
			final int insertIndex = -index - 1;
			freeCellByShifting(insertIndex);
			prices[insertIndex] = price;
			volumes[insertIndex] = volume;
			size++;
		}
	}

	private void freeCellByShifting(int insertIndex) {
		if (reversed) {
			System.arraycopy(prices, insertIndex, prices, insertIndex - 1, insertIndex + 1);
			System.arraycopy(volumes, insertIndex, volumes, insertIndex - 1, insertIndex + 1);
		} else {
			System.arraycopy(prices, insertIndex, prices, insertIndex + 1, size - insertIndex);
			System.arraycopy(volumes, insertIndex, volumes, insertIndex + 1, size - insertIndex);
		}
	}

	private void expand() {
		final int newCapacity = prices.length * EXPANSION_NUMERATOR / EXPANSION_DENOMINATOR;

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

	public int getPrice(int index) {
		return prices[mapIndex(index)];
	}

	public int getVolume(int index) {
		return volumes[mapIndex(index)];
	}

	@Override
	public String toString() {
		return new StringBuilder().append("prices: ").append(Arrays.toString(prices)).append(", volumes: ")
				.append(Arrays.toString(volumes)).toString();
	}

}
