package com.streamlined.orderbook.hashtableimplementation;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OrderedLinkedList implements Iterable<Node> {

	private Node head;

	public OrderedLinkedList() {
	}

	public OrderedLinkedList(int[] orders, int[] volumes) {
		for (int k = 0; k < Math.min(orders.length, volumes.length); k++) {
			add(orders[k], volumes[k]);
		}
	}

	public boolean isEmpty() {
		return head == null;
	}

	public int getSize() {
		int count = 0;
		for (Node node : this) {
			count++;
		}
		return count;
	}

	public void add(int order, int size) {
		Node newNode = new Node(order, size);
		if (head == null) {
			head = newNode;
		} else if (newNode.precedes(head)) {
			newNode.setNextNode(head);
			head = newNode;
		} else {
			Node node = head;
			do {
				Node nextNode = node.getNextNode();
				if (nextNode == null) {
					node.setNextNode(newNode);
					break;
				} else if (newNode.precedes(nextNode)) {
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
			if (!node.precedes(order)) {
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
			if (!node.precedes(order)) {
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
			if (!node.precedes(order)) {
				return null;
			}
		}
		return null;
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