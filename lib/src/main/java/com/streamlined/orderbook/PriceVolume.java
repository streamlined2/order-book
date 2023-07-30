package com.streamlined.orderbook;

class PriceVolume {

	private int price;
	private int volume;

	int getPrice() {
		return price;
	}

	int getVolume() {
		return volume;
	}

	void setPriceVolume(int price, int volume) {
		this.price = price;
		this.volume = volume;
	}

}
