package com.streamlined.orderbook;

public interface VolumeContainer {

	int getSize();

	int getCapacity();

	void set(int price, int volume);

	PriceVolume getBestPriceValue();

	int getBestPrice();

	int getVolumeByPrice(int price);

	int subtractVolumeForBestPrice(int subtractVolume);

	int getPrice(int index);

	int getVolume(int index);

	int indexOf(int price);

}
