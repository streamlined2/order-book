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

		if (size == 0 || belongsToRightHalf(prices[0], price)) {
			return -1;// insert at index 0 for an empty array or if price is less then left border
						// value
		}
		if (!belongsToLeftHalf(price, size)) {
			return -size - 1;// price greater than right border value, insert at rightmost free index
		}

		int leftIndex = 0;
		int rightIndex = size - 1;

		while (leftIndex <= rightIndex) {

			final int leftPrice = prices[leftIndex];
			final int rightPrice = prices[rightIndex];

			if (!withinRange(price, leftPrice, rightPrice)) {
				break;
			}

			final int middleIndex = getMiddleIndex(price, leftIndex, rightIndex, leftPrice, rightPrice);
			final int middlePrice = prices[middleIndex];

			if (price == middlePrice) {
				return middleIndex;
			}

			if (belongsToRightHalf(price, middlePrice)) {
				leftIndex = middleIndex + 1;
			} else if (belongsToLeftHalf(price, middleIndex)) {
				rightIndex = middleIndex - 1;
			} else {
				return -middleIndex - 1;// price does not belong to any half, insert between them
			}

		}

		return -leftIndex - 1;// value not found, insert at leftIndex position
	}

	private boolean belongsToRightHalf(int price, final int middlePrice) {
		if (reversed) {
			return middlePrice > price;
		} else {
			return middlePrice < price;
		}
	}

	private boolean belongsToLeftHalf(int price, final int middleIndex) {
		if (middleIndex > 0) {
			final int leftIndex = middleIndex - 1;
			final int leftPrice = prices[leftIndex];
			if (reversed) {
				return price >= leftPrice;
			} else {
				return price <= leftPrice;
			}
		}
		return false;
	}

	private boolean withinRange(int price, int leftPrice, int rightPrice) {
		if (reversed) {
			return rightPrice <= price && price <= leftPrice;
		} else {
			return leftPrice <= price && price <= rightPrice;
		}
	}

	private int getMiddleIndex(int price, int leftIndex, int rightIndex, final int leftPrice, final int rightPrice) {
		return rightPrice == leftPrice ? (leftIndex + rightIndex) >> 1
				: getMiddleIndexForPrice(price, leftIndex, rightIndex, leftPrice, rightPrice);
	}

	private int getMiddleIndexForPrice(int price, int leftIndex, int rightIndex, int leftPrice, int rightPrice) {
		long priceDiff;
		long maxPriceDiff;
		if (reversed) {
			priceDiff = leftPrice - price;
			maxPriceDiff = leftPrice - rightPrice;
		} else {
			priceDiff = price - leftPrice;
			maxPriceDiff = rightPrice - leftPrice;
		}
		return leftIndex + (int) ((rightIndex - leftIndex) * priceDiff / maxPriceDiff);
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
		return null;// violates clean code convention
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
