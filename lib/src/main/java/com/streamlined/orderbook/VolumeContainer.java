package com.streamlined.orderbook;

public interface VolumeContainer {

	int VOLUME_VALUE_ABSENT = -1;
	int PRICE_VALUE_ABSENT = -1;

	int getSize();

	void set(int price, int volume);

	PriceVolume getBestPriceValue();

	int getBestPrice();

	int getVolumeByPrice(int price);

	int subtractVolumeForBestPrice(int subtractVolume);

}
