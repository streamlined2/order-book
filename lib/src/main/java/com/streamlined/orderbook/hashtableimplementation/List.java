package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.PriceVolume;

public interface List {

	boolean isEmpty();

	int getSize();

	void setAdd(int order, int size);

	boolean setVolume(int order, int volume);

	boolean remove(int order);

	PriceVolume getPriceVolumeByOrder(int order);

	PriceVolume getFirstNonEmptyNode();

	SubtractionResult subtractVolume(int subtractVolume);

	static class SubtractionResult {
		int subtractedVolume;
		int lastCheckedOrder;
	}

}
