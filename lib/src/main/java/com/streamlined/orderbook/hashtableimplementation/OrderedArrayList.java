package com.streamlined.orderbook.hashtableimplementation;

import java.util.Arrays;

import com.streamlined.orderbook.PriceVolume;

public class OrderedArrayList implements List {

	private static final SubtractionResult subtractionResult = new SubtractionResult();
	private static final PriceVolume priceVolume = new PriceVolume();

	private boolean ascending;
	private final int[] prices;
	private final int[] volumes;
	private int size;

	public OrderedArrayList(boolean ascending, int capacity) {
		this.ascending = ascending;
		this.prices = new int[capacity];
		this.volumes = new int[capacity];
	}

	public OrderedArrayList(boolean ascending, int capacity, int price, int volume) {
		this(ascending, capacity);
		setAdd(price, volume);
	}

	public OrderedArrayList(boolean ascending, int[] prices, int[] volumes) {
		this(ascending, prices.length);
		System.arraycopy(prices, 0, this.prices, 0, prices.length);
		System.arraycopy(volumes, 0, this.volumes, 0, prices.length);
		size = prices.length;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	public void initialize(boolean ascending) {
		this.ascending = ascending;
		this.size = 0;
	}

	public boolean isFull() {
		return size == prices.length;
	}

	@Override
	public int getSize() {
		return size;
	}

	public int getPrice(int index) {
		return prices[index];
	}

	public int getVolume(int index) {
		return volumes[index];
	}

	@Override
	public int setAdd(int price, int volume) {
		final int index = Arrays.binarySearch(prices, 0, size, price);
		if (index >= 0) {
			volumes[index] = volume;
			return index;
		} else if (isFull()) {
			return PRICE_NOT_FOUND;
		}
		final int insertionIndex = -index - 1;
		System.arraycopy(prices, insertionIndex, prices, insertionIndex + 1, size - insertionIndex);
		prices[insertionIndex] = price;
		System.arraycopy(volumes, insertionIndex, volumes, insertionIndex + 1, size - insertionIndex);
		volumes[insertionIndex] = volume;
		size++;
		return insertionIndex;
	}

	@Override
	public boolean remove(int price) {
		final int index = Arrays.binarySearch(prices, 0, size, price);
		if (index >= 0) {
			removeAt(index);
			return true;
		}
		return false;
	}

	private void removeAt(int index) {
		System.arraycopy(prices, index + 1, prices, index, size - index - 1);
		System.arraycopy(volumes, index + 1, volumes, index, size - index - 1);
		size--;
	}

	@Override
	public PriceVolume getPriceVolumeByPrice(int price) {
		final int index = Arrays.binarySearch(prices, 0, size, price);
		if (index >= 0) {
			priceVolume.setPriceVolume(price, volumes[index]);
			return priceVolume;
		}
		return null;
	}

	@Override
	public PriceVolume getFirstNonEmptyNode() {
		if (ascending) {
			for (int index = 0; index < size; index++) {
				if (volumes[index] > 0) {
					priceVolume.setPriceVolume(prices[index], volumes[index]);
					return priceVolume;
				}
			}
		} else {
			for (int index = size - 1; index >= 0; index--) {
				if (volumes[index] > 0) {
					priceVolume.setPriceVolume(prices[index], volumes[index]);
					return priceVolume;
				}
			}
		}
		return null;
	}

	@Override
	public SubtractionResult subtractVolume(int subtractVolume) {
		int volumeLeftover = subtractVolume;
		int lastCheckedOrder = PRICE_NOT_FOUND;
		if (ascending) {
			while (size > 0) {
				lastCheckedOrder = prices[0];
				if (volumes[0] <= volumeLeftover) {
					volumeLeftover -= volumes[0];
					removeAt(0);
				} else {
					volumes[0] -= volumeLeftover;
					volumeLeftover = 0;
					break;
				}
			}
		} else {
			for (int k = size - 1; k >= 0; k--) {
				lastCheckedOrder = prices[k];
				if (volumes[k] <= volumeLeftover) {
					volumeLeftover -= volumes[k];
					size--;
				} else {
					volumes[k] -= volumeLeftover;
					volumeLeftover = 0;
					break;
				}
			}
		}
		subtractionResult.subtractedVolume = subtractVolume - volumeLeftover;
		subtractionResult.lastCheckedOrder = lastCheckedOrder;
		return subtractionResult;
	}

}
