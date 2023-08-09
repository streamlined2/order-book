package com.streamlined.orderbook;

public interface VolumeContainer {

	int getSize();

	void set(int price, int volume);

	PriceVolume getBestPriceValue();

	int getBestPrice();
	
	int getVolumeByPrice(int price);

	int subtractVolumeForBestPrice(int subtractVolume);

}
