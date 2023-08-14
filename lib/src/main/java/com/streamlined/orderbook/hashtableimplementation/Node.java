package com.streamlined.orderbook.hashtableimplementation;

public class Node {

	private Node nextNode;
	private final int order;
	private int volume;

	public Node(int order, int volume) {
		this.order = order;
		this.volume = volume;
	}

	public void setNextNode(Node node) {
		nextNode = node;
	}

	public Node getNextNode() {
		return nextNode;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getVolume() {
		return volume;
	}

	public int getOrder() {
		return order;
	}

	public boolean precedes(Node node) {
		return order < node.order;
	}

	public boolean precedes(int order) {
		return this.order < order;
	}

	@Override
	public String toString() {
		return String.format("[%d,%d]", order, volume);
	}

	@Override
	public int hashCode() {
		return order;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			Node node = (Node) o;
			return this.order == node.order;
		}
		return false;
	}

}