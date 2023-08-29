package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.PriceVolume;

public interface List {

	int PRICE_NOT_FOUND = -1;

	boolean isEmpty();

	int getSize();

	int setAdd(int order, int size);

	boolean remove(int order);

	PriceVolume getPriceVolumeByPrice(int order);

	PriceVolume getFirstNonEmptyNode();

	SubtractionResult subtractVolume(int subtractVolume);

	void initialize(boolean ascending);

	class SubtractionResult {
		int subtractedVolume;
		int lastCheckedOrder;
	}

}
