package com.streamlined.orderbook.hashtableimplementation;

import com.streamlined.orderbook.PriceVolume;
import com.streamlined.orderbook.VolumeContainer;

public class HashtableContainer implements VolumeContainer {

	private static final int INITIAL_CAPACITY = 100;

	private Node[] buckets;

	public HashtableContainer() {
		this(INITIAL_CAPACITY);
	}

	public HashtableContainer(int capacity) {
		buckets = new Node[capacity];
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void set(int price, int volume) {
		// TODO Auto-generated method stub

	}

	@Override
	public PriceVolume getBestPriceValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBestPrice() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVolumeByPrice(int price) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int subtractVolumeForBestPrice(int subtractVolume) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPrice(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVolume(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int indexOf(int price) {
		// TODO Auto-generated method stub
		return 0;
	}

}
