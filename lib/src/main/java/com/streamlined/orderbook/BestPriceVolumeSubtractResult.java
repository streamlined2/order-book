package com.streamlined.orderbook;

public class BestPriceVolumeSubtractResult {
	int lastCheckedPrice;
	int volumeSubtracted;

	public void setValues(int lastCheckedPrice, int volumeSubtracted) {
		this.lastCheckedPrice = lastCheckedPrice;
		this.volumeSubtracted = volumeSubtracted;
	}
	
	public int getVolumeSubtracted() {
		return volumeSubtracted;
	}
	
}
