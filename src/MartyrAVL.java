import java.io.*;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class MartyrAVL {
	private martyrAVLNode root;

	public MartyrAVL() {
		this.root = null;
	}

	public martyrAVLNode getRoot() {
		return root;
	}

	public void setRoot(martyrAVLNode root) {
		this.root = root;
	}

	public int getHeight() {
		return getHeight(root);
	}

	private int getHeight(martyrAVLNode node) {
		if (node == null) {
			return -1;
		}
		int leftHeight = getHeight(node.getLeft());
		int rightHeight = getHeight(node.getRight());
		return Math.max(leftHeight, rightHeight) + 1;
	}

	private int getBalance(martyrAVLNode node) {
		if (node == null) {
			return 0;
		}
		return getHeight(node.getLeft()) - getHeight(node.getRight());
	}

	public void insert(Martyr martyr) {
		root = insertNode(root, martyr);
	}

	private martyrAVLNode insertNode(martyrAVLNode node, Martyr martyr) {
		if (node == null) {
			return new martyrAVLNode(martyr);
		}

		if (martyr.getName().compareTo(node.getMartyr().getName()) < 0) {
			node.setLeft(insertNode(node.getLeft(), martyr));
		} else if (martyr.getName().compareTo(node.getMartyr().getName()) > 0) {
			node.setRight(insertNode(node.getRight(), martyr));
		}
		node.setHeight(1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight())));

		int balance = getBalance(node);

		if (balance > 1) {
			if (martyr.getName().compareTo(node.getLeft().getMartyr().getName()) < 0) {
				return rotateWithLeftChild(node);
			} else {
				return DoubleWithLeftChild(node);
			}
		}

		if (balance < -1) {
			if (martyr.getName().compareTo(node.getRight().getMartyr().getName()) > 0) {
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
	private martyrAVLNode rotateWithLeftChild(martyrAVLNode k2) {
		martyrAVLNode k1 = k2.getLeft();
		k2.setLeft(k1.getRight());
		k1.setRight(k2);
		k2.setHeight(Math.max(getHeight(k2.getLeft()), getHeight(k2.getRight())) + 1);
		k1.setHeight(Math.max(getHeight(k1.getLeft()), k2.getHeight()) + 1);
		return k1;
	}

	/**
	 * Perform a double rotation (left-right) on a node in the AVL tree.
	 *
	 * @param k3 the node to perform the rotation on
	 * @return the updated node after rotation
	 */
	private martyrAVLNode rotateWithRightChild(martyrAVLNode k1) {
		martyrAVLNode k2 = k1.getRight();
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
	private martyrAVLNode DoubleWithLeftChild(martyrAVLNode k3) {
		k3.setLeft(rotateWithRightChild(k3.getLeft()));
		return rotateWithLeftChild(k3);
	}

	/**
	 * Perform a double rotation (left-right) on a node in the AVL tree.
	 *
	 * @param k3 the node to perform the rotation on
	 * @return the updated node after rotation
	 */
	private martyrAVLNode DoubleWithRightChild(martyrAVLNode k1) {
		k1.setRight(rotateWithLeftChild(k1.getRight()));
		return rotateWithRightChild(k1);
	}

	public boolean search(Martyr martyr) {
		return searchRecursive(root, martyr);
	}

	/**
	 * Perform a double rotation (left-right) on a node in the AVL tree.
	 *
	 * @param k3 the node to perform the rotation on
	 * @return the updated node after rotation
	 */
	private boolean searchRecursive(martyrAVLNode node, Martyr martyr) {
		if (node == null) {
			return false; // Martyr not found in the AVL tree
		}

		int comparison = martyr.getName().compareTo(node.getMartyr().getName());

		if (comparison < 0) {
			return searchRecursive(node.getLeft(), martyr);
		} else if (comparison > 0) {
			return searchRecursive(node.getRight(), martyr);
		} else {
			// Found a martyr with the same name, check for other attributes
			if (martyr.getAge() == node.getMartyr().getAge()
					&& martyr.getLocation().equals(node.getMartyr().getLocation())
					&& martyr.getDate().isEqual(node.getMartyr().getDate())
					&& martyr.getGender() == node.getMartyr().getGender()) {
				return true; // Duplicate martyr found
			} else {
				// Same name but different attributes, continue searching
				return searchRecursive(node.getRight(), martyr);
			}
		}
	}

	public void delete(String martyrName) {
		root = deleteNode(root, martyrName);
	}

	private martyrAVLNode deleteNode(martyrAVLNode node, String martyrName) {
		if (node == null) {
			return null; // Martyr not found
		}

		int comparison = martyrName.compareTo(node.getMartyr().getName());

		if (comparison < 0) {
			node.setLeft(deleteNode(node.getLeft(), martyrName));
		} else if (comparison > 0) {
			node.setRight(deleteNode(node.getRight(), martyrName));
		} else {
			// Found the martyr to delete
			if (node.getLeft() == null || node.getRight() == null) {
				// Martyr has 0 or 1 child
				martyrAVLNode child = (node.getLeft() != null) ? node.getLeft() : node.getRight();
				if (child == null) {
					// Martyr has no child
					node = null;
				} else {
					// Martyr has one child
					node = child;
				}
			} else {
				// Martyr has 2 children
				martyrAVLNode successor = findSuccessor(node.getRight());
				node.getMartyr().setName(successor.getMartyr().getName());
				node.setRight(deleteNode(node.getRight(), successor.getMartyr().getName()));
			}
		}

		if (node != null) {
			node.setHeight(1 + Math.max(getHeight(node.getLeft()), getHeight(node.getRight())));
			int balance = getBalance(node);

			if (balance > 1) {
				if (getBalance(node.getLeft()) >= 0) {
					node = rotateWithLeftChild(node);
				} else {
					node = DoubleWithLeftChild(node);
				}
			} else if (balance < -1) {
				if (getBalance(node.getRight()) <= 0) {
					node = rotateWithRightChild(node);
				} else {
					node = DoubleWithRightChild(node);
				}
			}
		}

		return node;
	}

	private martyrAVLNode findSuccessor(martyrAVLNode node) {
		if (node.getLeft() == null) {
			return node;
		}
		return findSuccessor(node.getLeft());
	}

	public void update(Martyr oldMartyr, Martyr newMartyr) {
		delete(oldMartyr.getName());
		insert(newMartyr);
	}

	public void inorderTraversal(martyrAVLNode node, ComboBox<String> comboBox) {
		if (node != null) {
			inorderTraversal(node.getLeft(), comboBox);
			comboBox.getItems().add(node.getMartyr().getName());
			inorderTraversal(node.getRight(), comboBox);
		}
	}

	public martyrAVLNode searchByName(String name) {
		return searchByNameRecursive(root, name);
	}

	public martyrAVLNode searchByNameRecursive(martyrAVLNode node, String name) {
		if (node == null || name.equals(node.getMartyr().getName())) {
			return node;
		}

		int comparison = name.compareTo(node.getMartyr().getName());

		if (comparison < 0) {
			return searchByNameRecursive(node.getLeft(), name);
		} else {
			return searchByNameRecursive(node.getRight(), name);
		}
	}

	public void saveToFile(File file) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			inOrderTraversal(root, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void inOrderTraversal(martyrAVLNode node, BufferedWriter writer) throws IOException {
		if (node == null) {
			return;
		}

		inOrderTraversal(node.getLeft(), writer);

		Martyr martyr = node.getMartyr();
		String line = martyr.getName() + "," + martyr.getAge() + "," + martyr.getLocation() + "," + martyr.getDate()
				+ "," + martyr.getGender();
		writer.write(line);
		writer.newLine();

		inOrderTraversal(node.getRight(), writer);
	}

	/**
	 * Perform a double rotation (left-right) on a node in the AVL tree.
	 *
	 * @param k3 the node to perform the rotation on
	 * @return the updated node after rotation
	 */
	public int getNodeCount() {
		return getNodeCount(root);
	}

	private int getNodeCount(martyrAVLNode node) {
		if (node == null) {
			return 0;
		}
		int leftCount = getNodeCount(node.getLeft());
		int rightCount = getNodeCount(node.getRight());
		return leftCount + rightCount + 1;
	}

	public void printLevelOrder(TextArea textArea) {
		if (root == null) {
			textArea.appendText("The AVL tree is empty.");
			return;
		}

		int height = getHeight(root);

		for (int level = 0; level <= height; level++) {
			printNodesAtLevel(root, level, textArea);
			textArea.appendText(System.lineSeparator());
		}
	}

	private void printNodesAtLevel(martyrAVLNode node, int level, TextArea textArea) {
		if (node == null) {
			return;
		}

		if (level == 1) {
			Martyr martyr = node.getMartyr();
			textArea.appendText("Name: " + martyr.getName() + " ");
			textArea.appendText("Age: " + martyr.getAge() + " ");
			textArea.appendText("Death: " + martyr.getDate() + " ");
			textArea.appendText("Gender: " + martyr.getGender() + "\n");
			//textArea.appendText("\n");
		} else if (level > 1) {
			printNodesAtLevel(node.getLeft(), level - 1, textArea);
			printNodesAtLevel(node.getRight(), level - 1, textArea);
		}
	}
	public String printLevelOrder() {
	    if (root == null) {
	        return "The AVL tree is empty.";
	    }

	    int height = getHeight(root);
	    StringBuilder sb = new StringBuilder();

	    for (int level = 0; level <= height; level++) {
	        printNodesAtLevel(root, level, sb);
	       // sb.append(System.lineSeparator()); // Append a new line after each level
	    }

	    return sb.toString();
	}

	private void printNodesAtLevel(martyrAVLNode node, int level, StringBuilder sb) {
	    if (node == null) {
	        return;
	    }

	    if (level == 0) {
	        Martyr martyr = node.getMartyr();
	        sb.append("Name: ").append(martyr.getName()+" ");
	        sb.append("Age: ").append(martyr.getAge()+" ");
	        sb.append("Death: ").append(martyr.getDate()+" ");
	        sb.append("Gender: ").append(martyr.getGender()).append(System.lineSeparator());
	    } else if (level > 0) {
	        printNodesAtLevel(node.getLeft(), level - 1, sb);
	        printNodesAtLevel(node.getRight(), level - 1, sb);
	    }
	}



}
