package com.streamlined.orderbook.hashtableimplementation;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.streamlined.orderbook.PriceVolume;

public class OrderedLinkedList implements List, Iterable<OrderedLinkedList.Node> {

	private static final SubtractionResult subtractionResult = new SubtractionResult();
	private static final PriceVolume priceVolume = new PriceVolume();

	private Node head;
	private boolean ascending;

	public OrderedLinkedList(boolean ascending) {
		this.ascending = ascending;
	}

	public OrderedLinkedList(boolean ascending, int price, int volume) {
		this.ascending = ascending;
		this.head = new Node(price, volume);
	}

	public OrderedLinkedList(boolean ascending, int[] orders, int[] volumes) {
		this.ascending = ascending;
		for (int k = 0; k < Math.min(orders.length, volumes.length); k++) {
			setAdd(orders[k], volumes[k]);
		}
	}

	@Override
	public boolean isEmpty() {
		return head == null;
	}

	@Override
	public int getSize() {
		int count = 0;
		for (Node node = head; node != null; node = node.nextNode) {
			count++;
		}
		return count;
	}

	@Override
	public int setAdd(int order, int size) {
		int index = 0;
		if (head == null) {
			head = new Node(order, size);
		} else if (precedes(order, head)) {
			head = new Node(order, size, head);
		} else if (order == head.order) {
			head.volume = size;
		} else {
			Node node = head;
			do {
				index++;
				Node nextNode = node.nextNode;
				if (nextNode == null) {
					node.nextNode = new Node(order, size);
					break;
				} else if (precedes(order, nextNode)) {
					node.nextNode = new Node(order, size, nextNode);
					break;
				} else if (order == nextNode.order) {
					nextNode.volume = size;
					break;
				}
				node = nextNode;
			} while (true);
		}
		return index;
	}

	@Override
	public int addLast(int price, int volume) {
		return setAdd(price, volume);
	}

	public boolean setVolume(int order, int volume) {
		for (Node node = head; node != null; node = node.nextNode) {
			if (node.order == order) {
				node.volume = volume;
				return true;
			}
			if (!precedes(node, order)) {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean remove(int order) {
		for (Iterator<Node> i = iterator(); i.hasNext();) {
			Node node = i.next();
			if (node.order == order) {
				i.remove();
				return true;
			}
			if (!precedes(node, order)) {
				return false;
			}
		}
		return false;
	}

	@Override
	public PriceVolume getPriceVolumeByPrice(int order) {
		for (Node node = head; node != null; node = node.nextNode) {
			if (node.order == order) {
				priceVolume.setPriceVolume(node.order, node.volume);
				return priceVolume;
			}
			if (!precedes(node, order)) {
				return null;
			}
		}
		return null;
	}

	@Override
	public PriceVolume getFirstNonEmptyNode() {
		for (Node node = head; node != null; node = node.nextNode) {
			if (node.volume > 0) {
				priceVolume.setPriceVolume(node.order, node.volume);
				return priceVolume;
			}
		}
		return null;
	}

	@Override
	public SubtractionResult subtractVolume(int subtractVolume) {
		int volumeLeftover = subtractVolume;
		int lastCheckedOrder = 0;
		while (head != null) {
			lastCheckedOrder = head.order;
			if (head.volume <= volumeLeftover) {
				volumeLeftover -= head.volume;
				head = head.nextNode;
			} else {
				head.volume -= volumeLeftover;
				volumeLeftover = 0;
				break;
			}
		}
		subtractionResult.subtractedVolume = subtractVolume - volumeLeftover;
		subtractionResult.lastCheckedOrder = lastCheckedOrder;
		return subtractionResult;
	}

	@Override
	public Iterator<Node> iterator() {
		return new Iterator<>() {

			private Node nextNodeToReturn = head;
			private Node lastReturnedNode;
			private Node previousToLastReturnedNode;

			@Override
			public boolean hasNext() {
				return nextNodeToReturn != null;
			}

			@Override
			public Node next() {
				if (!hasNext())
					throw new NoSuchElementException("no more elements in linked list");
				previousToLastReturnedNode = lastReturnedNode;
				lastReturnedNode = nextNodeToReturn;
				nextNodeToReturn = nextNodeToReturn.nextNode;
				return lastReturnedNode;
			}

			@Override
			public void remove() {
				if (lastReturnedNode == null) {
					throw new NoSuchElementException("next should be called before remove");
				}
				if (lastReturnedNode == head) {
					head = nextNodeToReturn;
					lastReturnedNode = previousToLastReturnedNode = null;
				} else if (previousToLastReturnedNode != null) {
					previousToLastReturnedNode.nextNode = nextNodeToReturn;
					lastReturnedNode = previousToLastReturnedNode;
					previousToLastReturnedNode = null;
				} else {
					throw new IllegalStateException("next should be called before remove");
				}
			}

		};
	}

	private boolean precedes(Node node, int order) {
		return ascending ? node.order < order : node.order > order;
	}

	private boolean precedes(int order, Node node) {
		return ascending ? order < node.order : order > node.order;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder("{");
		for (Node node : this) {
			b.append(node).append(",");
		}
		if (!isEmpty()) {
			b.deleteCharAt(b.length() - 1);
		}
		b.append("}");
		return b.toString();
	}

	protected static class Node {

		private Node nextNode;
		private final int order;
		private int volume;

		public Node(int order, int volume) {
			this.order = order;
			this.volume = volume;
		}

		public Node(int order, int volume, Node nextNode) {
			this.order = order;
			this.volume = volume;
			this.nextNode = nextNode;
		}

		public int getOrder() {
			return order;
		}

		public int getVolume() {
			return volume;
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

	@Override
	public void initialize(boolean ascending) {
		this.ascending = ascending;
		this.head = null;
	}

}
