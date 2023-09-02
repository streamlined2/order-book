package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.PriceVolume;

public class OrderedArrayList implements List {

	private static final SubtractionResult subtractionResult = new SubtractionResult();
	private static final PriceVolume priceVolume = new PriceVolume();

	private static final int UNDEFINED_INDEX = -1;

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
		addLast(price, volume);
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

	private int seek(int price) {
		for (int k = 0; k < size; k++) {
			if (prices[k] == price) {
				return k;
			}
			if (prices[k] > price) {
				return -(k + 1);
			}
		}
		return -(size + 1);
	}

	private int binarySeek(int price) {
		int lowIndex = 0;
		int highIndex = size - 1;

		while (lowIndex <= highIndex) {
			int midIndex = (lowIndex + highIndex) >>> 1;
			int midValue = prices[midIndex];

			if (midValue < price) {
				lowIndex = midIndex + 1;
			} else if (midValue > price) {
				highIndex = midIndex - 1;
			} else {
				return midIndex;
			}
		}
		return -(lowIndex + 1);
	}

	@Override
	public int setAdd(int price, int volume) {
		final int index = binarySeek(price);// Arrays.binarySearch(prices, 0, size, price);//
		if (index >= 0) {
			volumes[index] = volume;
			return index;
		} else if (isFull()) {
			return PRICE_NOT_FOUND;
		}
		final int insertionIndex = -index - 1;
		if (size > insertionIndex) {
			System.arraycopy(prices, insertionIndex, prices, insertionIndex + 1, size - insertionIndex);
			System.arraycopy(volumes, insertionIndex, volumes, insertionIndex + 1, size - insertionIndex);
		}
		prices[insertionIndex] = price;
		volumes[insertionIndex] = volume;
		size++;
		return insertionIndex;
	}

	@Override
	public int addLast(int price, int volume) {
		if (isFull()) {
			return PRICE_NOT_FOUND;
		}
		final int insertionIndex = size;
		prices[insertionIndex] = price;
		volumes[insertionIndex] = volume;
		size++;
		return insertionIndex;
	}

	@Override
	public boolean remove(int price) {
		final int index = seek(price);
		if (index < 0) {
			return false;
		}
		removeAt(index);
		return true;
	}

	private void removeAt(int index) {
		System.arraycopy(prices, index + 1, prices, index, size - index - 1);
		System.arraycopy(volumes, index + 1, volumes, index, size - index - 1);
		size--;
	}

	@Override
	public PriceVolume getPriceVolumeByPrice(int price) {
		final int index = binarySeek(price);// Arrays.binarySearch(prices, 0, size, price);//
		if (index < 0) {
			return null;
		}
		priceVolume.setPriceVolume(price, volumes[index]);
		return priceVolume;
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
			int lastEmptyNodeIndex = UNDEFINED_INDEX;
			for (int k = 0; k < size && volumeLeftover > 0; k++) {
				lastCheckedOrder = prices[k];
				if (volumes[k] <= volumeLeftover) {
					volumeLeftover -= volumes[k];
					lastEmptyNodeIndex = k;
				} else {
					volumes[k] -= volumeLeftover;
					volumeLeftover = 0;
					break;
				}
			}
			removeRange(0, lastEmptyNodeIndex);
		} else {
			for (int k = size - 1; k >= 0 && volumeLeftover > 0; k--) {
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

	private void removeRange(int startIndex, int finishIndex) {
		if (finishIndex != UNDEFINED_INDEX) {
			final int copyStartIndex = finishIndex + 1;
			if (copyStartIndex < size) {
				final int count = size - copyStartIndex;
				System.arraycopy(prices, copyStartIndex, prices, startIndex, count);
				System.arraycopy(volumes, copyStartIndex, volumes, startIndex, count);
			}
			size -= finishIndex - startIndex + 1;
		}
	}

}
