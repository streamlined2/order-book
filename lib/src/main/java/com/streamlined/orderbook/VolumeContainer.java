package com.streamlined.orderbook;

import java.util.Arrays;

public class VolumeContainer {

	private static final int EXPANSION_NUMERATOR = 110;
	private static final int EXPANSION_DENOMINATOR = 100;
	private static final int EXPANSION_CONSTANT = 1;

	private static final PriceVolume lastPriceVolume = new PriceVolume();
	public static final int VOLUME_VALUE_ABSENT = -1;
	public static final int PRICE_VALUE_ABSENT = -1;

	private final boolean reversed;
	private int[] prices;
	private int[] volumes;
	private int size;

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
			set(prices[k], volumes[k]);
		}
	}

	int getCapacity() {
		return prices.length;
	}

	public int getSize() {
		return size;
	}

	public void set(int price, int volume) {
		final int index = indexOf(price);
		if (index == size - 1 && volume == 0) {
			size--;
			removeEmptyVolumeItems(index - 1);
		} else if (index >= 0) {
			volumes[index] = volume;
		} else {
			freeCellAndStorePriceVolume(index, price, volume);
		}
	}

	private void removeEmptyVolumeItems(final int startIndex) {
		for (int k = startIndex; k >= 0 && volumes[k] == 0; k--) {
			size--;
		}
	}

	private void freeCellAndStorePriceVolume(final int index, int price, int volume) {
		final int insertIndex = -index - 1;
		freeCellByShifting(insertIndex);
		prices[insertIndex] = price;
		volumes[insertIndex] = volume;
	}

	private void freeCellByShifting(int insertIndex) {
		final int nextIndex = insertIndex + 1;
		final int firstFreeIndex = getFirstFreeCellIndex(nextIndex);
		if (firstFreeIndex == size) {
			if (size == prices.length) {
				expand();
			}
			size++;
		}
		System.arraycopy(prices, insertIndex, prices, nextIndex, firstFreeIndex - insertIndex);
		System.arraycopy(volumes, insertIndex, volumes, nextIndex, firstFreeIndex - insertIndex);
	}

	private int getFirstFreeCellIndex(int startIndex) {
		for (int k = startIndex; k < size; k++) {
			if (volumes[k] <= 0) {
				return k;
			}
		}
		return size;
	}

	private void expand() {
		int newCapacity = prices.length * EXPANSION_NUMERATOR / EXPANSION_DENOMINATOR;
		newCapacity = newCapacity > prices.length ? newCapacity : prices.length + EXPANSION_CONSTANT;

		final int[] newPrices = new int[newCapacity];
		System.arraycopy(prices, 0, newPrices, 0, prices.length);
		prices = newPrices;

		final int[] newVolumes = new int[newCapacity];
		System.arraycopy(volumes, 0, newVolumes, 0, volumes.length);
		volumes = newVolumes;
	}

	int indexOf(int price) {

		if (size == 0 || isOutOfLeftBorder(price)) {
			return -1;// insert at index 0 for empty array
		}
		if (isOutOfRightBorder(price)) {
			return -size - 1;// not found, insert at rightmost free index
		}

		int leftIndex = 0;
		int rightIndex = size - 1;

		while (leftIndex <= rightIndex && isWithinRange(price, leftIndex, rightIndex)) {

			final int middleIndex = getMiddleIndex(price, leftIndex, rightIndex);
			final int middlePrice = prices[middleIndex];

			if (price == middlePrice) {
				return middleIndex;
			}

			final boolean belongsToRightHalf = belongsToRightHalf(price, middlePrice);
			final boolean belongsToLeftHalf = belongsToLeftHalf(price, middleIndex);

			if (!belongsToRightHalf && !belongsToLeftHalf) {
				return -middleIndex - 1;
			} else if (belongsToRightHalf) {
				leftIndex = middleIndex + 1;
			} else {
				rightIndex = middleIndex - 1;
			}

		}

		return -leftIndex - 1;// value not found, return index to place value
	}

	private boolean belongsToRightHalf(int price, final int middlePrice) {
		return reversed && middlePrice > price || !reversed && middlePrice < price;
	}

	private boolean belongsToLeftHalf(int price, final int middleIndex) {
		if (middleIndex > 0) {
			final int leftIndex = middleIndex - 1;
			final int leftPrice = prices[leftIndex];
			return reversed && price >= leftPrice || !reversed && price <= leftPrice;
		}
		return false;
	}

	private boolean isOutOfLeftBorder(int price) {
		return belongsToRightHalf(prices[0], price);
	}

	private boolean isOutOfRightBorder(int price) {
		return !belongsToLeftHalf(price, size);
	}

	private boolean isWithinRange(int price, int leftIndex, int rightIndex) {
		if (reversed) {
			return prices[rightIndex] <= price && price <= prices[leftIndex];
		} else {
			return prices[leftIndex] <= price && price <= prices[rightIndex];
		}
	}

	private int getMiddleIndex(int price, int leftIndex, int rightIndex) {
		final int leftPrice = prices[leftIndex];
		final int rightPrice = prices[rightIndex];
		if (rightPrice == leftPrice) {
			return (leftIndex + rightIndex) >> 1;
		}
		long priceDiff;
		long maxPriceDiff;
		if (reversed) {
			priceDiff = leftPrice - price;
			maxPriceDiff = leftPrice - rightPrice;
		} else {
			priceDiff = price - leftPrice;
			maxPriceDiff = rightPrice - leftPrice;
		}
		return (int) (priceDiff * (rightIndex - leftIndex) / maxPriceDiff + leftIndex);
	}

	int getPrice(int index) {
		return prices[index];
	}

	int getVolume(int index) {
		return volumes[index];
	}

	public PriceVolume getBestPriceValue() {
		final int index = getBestPriceIndex();
		if (isIndexValid(index)) {
			lastPriceVolume.setPriceVolume(prices[index], volumes[index]);
			return lastPriceVolume;// mutable singleton to avoid GC involvement
		}
		return null;// violates clear code convention
	}

	public int getBestPrice() {
		final int index = getBestPriceIndex();
		if (isIndexValid(index)) {
			return prices[index];
		}
		return PRICE_VALUE_ABSENT;
	}

	int getBestPriceIndex() {
		return size - 1;
	}

	int getBestPriceVolume() {
		return volumes[getBestPriceIndex()];
	}

	private boolean isIndexValid(int index) {
		return 0 <= index && index < size;
	}

	public int getVolumeByPrice(int price) {
		final int index = indexOf(price);
		if (isIndexValid(index)) {
			return volumes[index];
		}
		return VOLUME_VALUE_ABSENT;// magic value to avoid exception handling
	}

	public int subtractVolumeForBestPrice(int subtractVolume) {
		int index = getBestPriceIndex();
		if (isIndexValid(index)) {
			int toSubtractVolume = subtractVolume;
			int subtractedVolume = 0;
			do {
				final int volume = volumes[index];
				if (volume <= toSubtractVolume) {
					subtractedVolume += volume;
					toSubtractVolume -= volume;
					size--;
				} else {
					subtractedVolume += toSubtractVolume;
					volumes[index] -= toSubtractVolume;
					break;
				}
				index--;
			} while (index >= 0);
			return subtractedVolume;
		}
		return VOLUME_VALUE_ABSENT;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("prices: ").append(Arrays.toString(prices)).append(", volumes: ")
				.append(Arrays.toString(volumes)).toString();
	}

}
