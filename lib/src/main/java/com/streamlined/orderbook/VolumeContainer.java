package com.streamlined.orderbook;

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
		if (size == prices.length) {
			expand();
		}
		final int index = mapIndex(size++);
		prices[index] = price;
		volumes[index] = volume;
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

}
