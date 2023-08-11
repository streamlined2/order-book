package com.streamlined.orderbook.arrayimplementation;

public class NaturalOrderContainer extends ArrayContainer {

	public NaturalOrderContainer(int initialCapacity) {
		super(initialCapacity);
	}

	public NaturalOrderContainer(int[] prices, int[] volumes) {
		super(prices, volumes);
	}

	protected boolean belongsToRightHalf(int price, final int middlePrice) {
		return middlePrice < price;
	}

	protected boolean lessOrEqualThanHighBorder(int price, int border) {
		return price <= border;
	}

	protected boolean withinRange(int price, int leftPrice, int rightPrice) {
		return leftPrice <= price && price <= rightPrice;
	}

	protected int getMiddleIndexForPrice(int price, int leftIndex, int rightIndex, int leftPrice, int rightPrice) {
		long priceDiff = price - leftPrice;
		long maxPriceDiff = rightPrice - leftPrice;
		return leftIndex + (int) ((rightIndex - leftIndex) * priceDiff / maxPriceDiff);
	}

}
