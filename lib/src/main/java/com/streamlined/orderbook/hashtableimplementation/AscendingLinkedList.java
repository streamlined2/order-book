package com.streamlined.orderbook.hashtableimplementation;

public class AscendingLinkedList extends OrderedLinkedList {

	public AscendingLinkedList() {
	}

	public AscendingLinkedList(int[] orders, int[] volumes) {
		super(orders, volumes);
	}

	@Override
	protected boolean precedes(Node node1, Node node2) {
		return node1.getOrder() < node2.getOrder();
	}

	@Override
	protected boolean precedes(Node node, int order) {
		return node.getOrder() < order;
	}

	@Override
	protected boolean precedes(int order, Node node) {
		return order < node.getOrder();
	}

}