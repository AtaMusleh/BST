
public class Stack {
	private stackNode top;
	int size;

	public Stack() {
		this.top = null;
	}

	public boolean isEmpty() {
		return top == null;
	}

	public void push(Martyr martyr) {
		stackNode newNode = new stackNode(martyr);
		newNode.setNext(top);
		top = newNode;
		size++;
	}

	public Martyr pop() {
		if (isEmpty()) {
			return null; // Stack is empty
		}

		Martyr martyr = top.getMartyr();
		top = top.getNext();
		return martyr;
	}

	public Martyr peek() {
		if (isEmpty()) {
			return null; // Stack is empty
		}

		return top.getMartyr();
	}

	public void deleteMartyr(Martyr martyr) {
		if (isEmpty()) {
			System.out.println("Stack is empty.");
			return;
		}

		Stack tempStack = new Stack();

		while (!isEmpty()) {
			Martyr currentMartyr = pop();

			if (currentMartyr.equals(martyr)) {
				// Found the Martyr to delete, skip it
				continue;
			}

			tempStack.push(currentMartyr); // Move non-matching Martyrs to temporary stack
		}

		// Restore the original stack
		while (!tempStack.isEmpty()) {
			push(tempStack.pop());
		}
	}

	public void deleteNode(String value) {
		if (isEmpty()) {
			return; // Stack is empty, nothing to delete
		}

		stackNode previous = null;
		stackNode current = top;

		// Traverse the stack to find the node with the desired value
		while (current != null) {
			if (current.getMartyr().getName().equals(value)) {
				// Node with the desired value found, delete it
				if (previous == null) {
					// Deleting the top node
					top = current.getNext();
				} else {
					// Deleting a node in the middle or end
					previous.setNext(current.getNext());
				}
				return; // Node deleted, exit the method
			}

			previous = current;
			current = current.getNext();
		}
	}
	 public boolean contains(Martyr martyr) {
	        if (martyr == null) {
	            return false;
	        }

	        Stack tempStack = new Stack();

	        while (!this.isEmpty()) {
	            Martyr element = this.pop();
	            tempStack.push(element);
	            if (martyr.equals(element)) {
	                while (!tempStack.isEmpty()) {
	                    this.push(tempStack.pop());
	                }
	                return true;
	            }
	        }

	        while (!tempStack.isEmpty()) {
	            this.push(tempStack.pop());
	        }

	        return false;
	    }

}
