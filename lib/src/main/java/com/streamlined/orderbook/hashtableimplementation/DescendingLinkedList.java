package com.streamlined.orderbook.hashtableimplementation;

public class DescendingLinkedList extends OrderedLinkedList {

	public DescendingLinkedList() {
	}

	public DescendingLinkedList(int[] orders, int[] volumes) {
		super(orders, volumes);
	}

	@Override
	protected boolean precedes(Node node1, Node node2) {
		return node1.getOrder() > node2.getOrder();
	}

	@Override
	protected boolean precedes(Node node, int order) {
		return node.getOrder() > order;
	}

}
