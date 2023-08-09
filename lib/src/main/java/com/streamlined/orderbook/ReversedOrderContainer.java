package com.streamlined.orderbook;

public class ReversedOrderContainer extends ArrayContainer {

	public ReversedOrderContainer(int initialCapacity) {
		super(initialCapacity);
	}

	public ReversedOrderContainer(int[] prices, int[] volumes) {
		super(prices, volumes);
	}

	protected boolean belongsToRightHalf(int price, final int middlePrice) {
		return middlePrice > price;
	}

	protected boolean lessOrEqualThanHighBorder(int price, int border) {
		return price >= border;
	}

	protected boolean withinRange(int price, int leftPrice, int rightPrice) {
		return rightPrice <= price && price <= leftPrice;
	}

	protected int getMiddleIndexForPrice(int price, int leftIndex, int rightIndex, int leftPrice, int rightPrice) {
		long priceDiff = leftPrice - price;
		long maxPriceDiff = leftPrice - rightPrice;
		return leftIndex + (int) ((rightIndex - leftIndex) * priceDiff / maxPriceDiff);
	}

}
