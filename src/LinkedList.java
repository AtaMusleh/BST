
public class LinkedList {
	private LocationNode Front, Back;

	public LinkedList() {
		Front = Back = null;
	}

	public LocationNode getFront() {
		return Front;
	}

	public void setFront(LocationNode front) {
		Front = front;
	}

	public LocationNode getBack() {
		return Back;
	}

	public void setBack(LocationNode back) {
		Back = back;
	}

	public void insert(LocationNode newNode) {
		if (Front == null) {
			// First node in the list
			Front = Back = newNode;
			newNode.setNext(newNode); // Circular reference
			newNode.setPre(newNode); // Circular reference
		} else {
			LocationNode current = Front;
			while (current != null && current.getLocation().compareTo(newNode.getLocation()) < 0) {
				current = current.getNext();
				if (current == Front)
					break; // Reached the end of the circular list
			}

			if (current == null || current == Front) {
				// Insert at the front of the list
				newNode.setNext(Front);
				newNode.setPre(Back);
				Front.setPre(newNode);
				Back.setNext(newNode);
				Front = newNode;
			} else {
				// Insert in the middle or at the end of the list
				newNode.setNext(current);
				newNode.setPre(current.getPre());
				current.getPre().setNext(newNode);
				current.setPre(newNode);
			}
		}
	}

	public LocationNode search(String location) {
		if (Front == null) {
			return null; // List is empty
		}

		LocationNode current = Front;
		do {
			if (current.getLocation().getLocation().equalsIgnoreCase(location)) {
				return current;
			}
			current = current.getNext();
		} while (current != Front);

		return null; // Location not found
	}

	public void delete(String location) {
		if (Front == null) {
			return; // List is empty
		}

		LocationNode node = search(location);
		if (node == null) {
			return; // Node not found
		}

		if (Front == Back) {
			// Only one node in the list
			Front = Back = null;
		} else if (node == Front) {
			Front = node.getNext();
		} else if (node == Back) {
			Back = node.getPre();
		}

		node.getPre().setNext(node.getNext());
		node.getNext().setPre(node.getPre());
	}

	public boolean isEmpty() {
		return Front == null;
	}

}
