import java.time.LocalDate;

import javafx.scene.control.TextArea;

public class DateAVL {
	private dateAVLNode root;

	public DateAVL() {
		this.root = null;
	}

	public dateAVLNode getRoot() {
		return root;
	}

	public void setRoot(dateAVLNode root) {
		this.root = root;
	}


	public int getHeight() {
		return getHeight(root);
	}

	private int getHeight(dateAVLNode node) {
		if (node == null) {
			return -1;
		}
		int leftHeight = getHeight(node.getLeft());
		int rightHeight = getHeight(node.getRight());
		return Math.max(leftHeight, rightHeight) + 1;
	}

	private int getBalance(dateAVLNode node) {
		if (node == null) {
			return 0;
		}
		return getHeight(node.getLeft()) - getHeight(node.getRight());
	}
	/**
     * Perform a double rotation (left-right) on a node in the AVL tree.
     *
     * @param k3 the node to perform the rotation on
     * @return the updated node after rotation
     */
	public void insert(DateStack dateStack) {
		root = insertNode(root, dateStack);
	}

	private dateAVLNode insertNode(dateAVLNode node, DateStack dateStack) {
		if (node == null) {
			return new dateAVLNode(dateStack);
		}

		if (dateStack.getDate().compareTo(node.getDateStack().getDate()) < 0) {
			node.setLeft(insertNode(node.getLeft(), dateStack));
		} else if (dateStack.getDate().compareTo(node.getDateStack().getDate()) > 0) {
			node.setRight(insertNode(node.getRight(), dateStack));
		} else {
			node.getDateStack().getStack().push(dateStack.getStack().pop());
		}

		node.setHeight(1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight())));

		int balance = getBalance(node);

		if (balance > 1) {
			if (dateStack.getDate().compareTo(node.getLeft().getDateStack().getDate()) < 0) {
				return rotateWithLeftChild(node);
			} else {
				return DoubleWithLeftChild(node);
			}
		}

		if (balance < -1) {
			if (dateStack.getDate().compareTo(node.getRight().getDateStack().getDate()) > 0) {
				return rotateWithRightChild(node);
			} else {
				return DoubleWithRightChild(node);
			}
		}

		return node;
	}
	/**
     * Perform a double rotation (left-right) on a node in the AVL tree.
     *
     * @param k3 the node to perform the rotation on
     * @return the updated node after rotation
     */
	private dateAVLNode rotateWithLeftChild(dateAVLNode k2) {
		dateAVLNode k1 = k2.getLeft();
		k2.setLeft(k1.getRight());
		k1.setRight(k2);
		k2.setHeight(Math.max(getHeight(k2.getLeft()), getHeight(k2.getRight())) + 1);
		k1.setHeight(Math.max(getHeight(k1.getLeft()), k2.getHeight()) + 1);
		return k1;
	}

	private dateAVLNode rotateWithRightChild(dateAVLNode k1) {
		dateAVLNode k2 = k1.getRight();
		k1.setRight(k2.getLeft());
		k2.setLeft(k1);
		k1.setHeight(Math.max(getHeight(k1.getLeft()), getHeight(k1.getRight())) + 1);
		k2.setHeight(Math.max(getHeight(k2.getRight()), k1.getHeight()) + 1);
		return k2;
	}
	/**
     * Perform a double rotation (left-right) on a node in the AVL tree.
     *
     * @param k3 the node to perform the rotation on
     * @return the updated node after rotation
     */
	private dateAVLNode DoubleWithLeftChild(dateAVLNode k3) {
		k3.setLeft(rotateWithRightChild(k3.getLeft()));
		return rotateWithLeftChild(k3);
	}

	private dateAVLNode DoubleWithRightChild(dateAVLNode k1) {
		k1.setRight(rotateWithLeftChild(k1.getRight()));
		return rotateWithRightChild(k1);
	}
	/**
     * Perform a double rotation (left-right) on a node in the AVL tree.
     *
     * @param k3 the node to perform the rotation on
     * @return the updated node after rotation
     */

	public DateStack search(LocalDate date) {
		return searchRecursive(root, date);
	}

	private DateStack searchRecursive(dateAVLNode node, LocalDate date) {
		if (node == null) {
			return null; // Date not found in the AVL tree
		}

		int comparison = date.compareTo(node.getDateStack().getDate());

		if (comparison < 0) {
			return searchRecursive(node.getLeft(), date);
		} else if (comparison > 0) {
			return searchRecursive(node.getRight(), date);
		} else {
			return node.getDateStack(); // Date found in the AVL tree
		}
	}
	/**
     * Perform a double rotation (left-right) on a node in the AVL tree.
     *
     * @param k3 the node to perform the rotation on
     * @return the updated node after rotation
     */
	public String getDateWithMaxMartyrs() {
		if (root == null) {
			return null; // Return null if the AVL tree is empty
		}

		DateStack maxDateStack = null;
		int maxMartyrCount = 0;

		dateAVLNode current = root;

		while (current != null) {
			if (current.getLeft() == null) {
				// Process current node
				int martyrCount = current.getDateStack().getStack().size;
				if (martyrCount > maxMartyrCount) {
					maxMartyrCount = martyrCount;
					maxDateStack = current.getDateStack();
				}

				// Move to the right subtree
				current = current.getRight();
			} else {
				// Find the inorder predecessor of current node
				dateAVLNode predecessor = current.getLeft();

				// Traverse to the rightmost node in the left subtree
				while (predecessor.getRight() != null && predecessor.getRight() != current) {
					predecessor = predecessor.getRight();
				}

				if (predecessor.getRight() == null) {
					// Establish link to current node in inorder traversal
					predecessor.setRight(current);
					current = current.getLeft();
				} else {
					// Process current node
					predecessor.setRight(null);
					int martyrCount = current.getDateStack().getStack().size;
					if (martyrCount > maxMartyrCount) {
						maxMartyrCount = martyrCount;
						maxDateStack = current.getDateStack();
					}

					// Move to the right subtree
					current = current.getRight();
				}
			}
		}

		if (maxDateStack != null) {
			return maxDateStack.getDate() + "";
		} else {
			return null; // Return null if no date with martyrs found
		}
	}
	public void printAscendingOrder(TextArea textArea) {
	    if (root == null) {
	        textArea.appendText("The AVL tree is empty.");
	        return;
	    }

	    printAscendingOrder(root, textArea);
	}
	
	private void printAscendingOrder(dateAVLNode node, TextArea textArea) {
	    if (node != null) {
	        printAscendingOrder(node.getLeft(), textArea);
	        textArea.appendText(node.getDateStack().toString() + "\n");
	        printAscendingOrder(node.getRight(), textArea);
	    }
	}



}
