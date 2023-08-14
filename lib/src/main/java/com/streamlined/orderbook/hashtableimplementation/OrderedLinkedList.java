package com.streamlined.orderbook.hashtableimplementation;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedLinkedList implements Iterable<Node> {

	private Node head;
	private final boolean ascending;

	public OrderedLinkedList() {
		this(false);
	}

	public OrderedLinkedList(boolean ascending) {
		this.ascending = ascending;
	}

	public OrderedLinkedList(int[] orders, int[] volumes) {
		this(false, orders, volumes);
	}

	public OrderedLinkedList(boolean ascending, int[] orders, int[] volumes) {
		this.ascending = ascending;
		for (int k = 0; k < Math.min(orders.length, volumes.length); k++) {
			add(orders[k], volumes[k]);
		}
	}

	public boolean isEmpty() {
		return head == null;
	}

	public int getSize() {
		int count = 0;
		for (Node node = head; node != null; node = node.getNextNode()) {
			count++;
		}
		return count;
	}

	public void add(int order, int size) {
		Node newNode = new Node(order, size);
		if (head == null) {
			head = newNode;
		} else if (precedes(newNode, head)) {
			newNode.setNextNode(head);
			head = newNode;
		} else {
			Node node = head;
			do {
				Node nextNode = node.getNextNode();
				if (nextNode == null) {
					node.setNextNode(newNode);
					break;
				} else if (precedes(newNode, nextNode)) {
					node.setNextNode(newNode);
					newNode.setNextNode(nextNode);
					break;
				}
				node = nextNode;
			} while (true);
		}
	}

	public boolean setVolume(int order, int volume) {
		for (Node node = head; node != null; node = node.getNextNode()) {
			if (node.getOrder() == order) {
				node.setVolume(volume);
				return true;
			}
			if (!precedes(node, order)) {
				return false;
			}
		}
		return false;
	}

	public boolean remove(int order) {
		for (Iterator<Node> i = iterator(); i.hasNext();) {
			Node node = i.next();
			if (node.getOrder() == order) {
				i.remove();
				return true;
			}
			if (!precedes(node, order)) {
				return false;
			}
		}
		return false;
	}

	public Node getNodeByOrder(int order) {
		for (Node node = head; node != null; node = node.getNextNode()) {
			if (node.getOrder() == order) {
				return node;
			}
			if (!precedes(node, order)) {
				return null;
			}
		}
		return null;
	}

	public Node getFirstNonEmptyNode() {
		for (Node node = head; node != null; node = node.getNextNode()) {
			if (node.getVolume() > 0) {
				return node;
			}
		}
		return null;
	}

	public int subtractVolume(int subtractVolume) {
		int subtractedVolume = 0;
		for (Node node = head; node != null && subtractedVolume < subtractVolume; node = node.getNextNode()) {
			int volumeLeftover = subtractVolume - subtractedVolume;
			final int volume = node.getVolume();
			if (volume < volumeLeftover) {
				subtractedVolume += volume;
				node.setVolume(0);
			} else {
				subtractedVolume += volumeLeftover;
				node.setVolume(volume - volumeLeftover);
			}
		}
		return subtractedVolume;
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
				nextNodeToReturn = nextNodeToReturn.getNextNode();
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
					previousToLastReturnedNode.setNextNode(nextNodeToReturn);
					lastReturnedNode = previousToLastReturnedNode;
					previousToLastReturnedNode = null;
				} else {
					throw new IllegalStateException("next should be called before remove");
				}
			}

		};
	}

	private boolean precedes(Node node1, Node node2) {
		return precedes(node1, node2.getOrder());
	}

	private boolean precedes(Node node, int order) {
		return ascending ? node.getOrder() < order : node.getOrder() > order;
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

}
