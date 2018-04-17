package tree;

import java.util.Comparator;


public class SimpleRedBlackTree<K, V> {
    private Comparator<K> comparator;
    private int size;
    private Node root;

    public SimpleRedBlackTree() {
    }

    public SimpleRedBlackTree(Comparator comparator) {
        this.comparator = comparator;
    }

    public boolean put(K key, V value) {
        if (key == null)
            return false;
        Node find = find(key);
        Node newNode = new Node(key, value);
        if (size == 0) {
            root = newNode;
            root.setBlack();
            size++;
            return true;
        }
        if (!find.isLeaf()) {
            find.value = newNode.value;
            return true;
        }
        newNode.setParent(find.getParent());
        if (newNode.getParent().getLeft() == newNode)
            newNode.getParent().setLeft(newNode);
        else newNode.getParent().setRight(newNode);
        size++;
        if (!newNode.getGrandfather().isLeaf())
            fixRBT(newNode);
        return true;
    }

    private void fixRBT(Node node) {
        while (node.isRed()) {
            if (node.getParent() == node.getGrandfather().getLeft()) {
                Node anotherNode = node.getGrandfather().getRight();
                if (anotherNode.isRed()) {
                    node.getParent().setBlack();
                    anotherNode.setBlack();
                    node.getGrandfather().setRed();
                    node = node.getGrandfather();
                } else if (node == node.getParent().getRight()) {
                    node = node.getParent();
                    rotateLeft(node);
                }
                node.getParent().setBlack();
                node.getGrandfather().setRed();
                rotateRight(node.getGrandfather());
            } else {
                Node anotherNode = node.getGrandfather().getLeft();
                if (anotherNode.isRed()) {
                    node.getParent().setBlack();
                    anotherNode.setBlack();
                    node.getGrandfather().setRed();
                    node = node.getGrandfather();
                } else if (node == node.getParent().getLeft()) {
                    node = node.getParent();
                    rotateRight(node);
                }
                node.getParent().setBlack();
                node.getGrandfather().setRed();
                rotateLeft(node.getGrandfather());
            }
        }
        root.setBlack();
    }

    private void rotateLeft(Node node) {
        Node right = node.getRight();
        node.setRight(right.getLeft());

        if (!right.getLeft().isLeaf())
            right.getLeft().setParent(node);
        right.setParent(node.getParent());
        if (node.getParent().isLeaf())
            root = right;
        else if (node == node.getParent().getLeft())
            node.getParent().setLeft(right);
        else
            node.getParent().setRight(right);
        right.setLeft(node);
        node.setParent(right);
    }

    private void rotateRight(Node node) {
        Node left = node.getLeft();
        node.setLeft(left.getRight());

        if (!left.getRight().isLeaf())
            left.getRight().setParent(node);
        left.setParent(node.getParent());
        if (node.getParent().isLeaf())
            root = left;
        else if (node == node.getParent().getLeft())
            node.getParent().setLeft(left);
        else
            node.getParent().setRight(left);
        left.setRight(node);
        node.setParent(left);
    }

    private Node find(K key) {
        Node result = null;
        Node currentNode = root;
        if (size == 0)
            return result;
        if (comparator != null) {
            while (result == null) {
                if (currentNode.isLeaf()) {
                    result = currentNode;
                    break;
                }
                int compareRes = comparator.compare(key, (K) currentNode.key);
                if (compareRes == 0) {
                    result = currentNode;
                    break;
                } else if (compareRes < 0)
                    currentNode = currentNode.left;
                else currentNode = currentNode.right;
            }
        } else {
            while (result == null) {
                if (currentNode.isLeaf()) {
                    result = currentNode;
                    break;
                }
                int compareRes = ((Comparable) key).compareTo(currentNode.key);
                if (compareRes == 0) {
                    result = currentNode;
                    break;
                } else if (compareRes < 0)
                    currentNode = currentNode.left;
                else currentNode = currentNode.right;
            }
        }
        return result;
    }

    private void fixTree(Node node) {

    }

    private class Node<K, V> {
        private K key;
        private V value;
        private Node left;
        private Node right;
        private Node parent;
        private Color color;

        private Node() {
            color = Color.BLACK;
        }

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
            left = setLeaf();
            left.setParent(this);
            right = setLeaf();
            right.setParent(this);
            parent = setLeaf();
            color = Color.RED;
        }

        private K getKey() {
            return key;
        }

        private V getValue() {
            return value;
        }

        private boolean hasLeft() {
            return !left.isLeaf();
        }

        private boolean hasRight() {
            return !right.isLeaf();
        }

        private boolean hasParent() {
            return !parent.isLeaf();
        }

        private Node getLeft() {
            if (left == null)
                left = setLeaf();
            return left;
        }

        private Node getRight() {
            if (right == null)
                right = setLeaf();
            return right;
        }

        private Node getParent() {
            return parent;
        }

        private Node<K, V> setLeaf() {
            return new Node<K, V>();
        }

        private void setLeft(Node node) {
            if (left == null) {
                left = new Node();
                left.setParent(this);
            }
            left = node;
        }

        private void setRight(Node node) {
            if (right == null) {
                right = new Node();
                right.setParent(this);
            }
            right = node;
        }

        private void setParent(Node node) {
            parent = node;
        }

        private void setRed() {
            color = Color.RED;
        }

        private void setBlack() {
            color = Color.BLACK;
        }

        private boolean isLeaf() {
            return this == null || this.key == null;
        }

        private boolean isRed() {
            return color == Color.RED;
        }

        private boolean isBlack() {
            return color == Color.BLACK;
        }

        private Node getGrandfather() {
            return this.getParent().getParent();
        }

        private Node getNext() {
            Node result = null;
            Node currentNode = this;
            if (!currentNode.getRight().isLeaf()) {
                result = currentNode.getRight();
                while (!result.getLeft().isLeaf())
                    result = result.getLeft();
                return result;
            }
            result = currentNode.getParent();
            while (!result.isLeaf() && currentNode == result.getRight()) {
                currentNode = result;
                result = result.getParent();
            }
            return result;
        }

        private void setValues(Node<K, V> node) {
            if (this.isLeaf()) {
                setLeft(node.getLeft());
                node.getLeft().setParent(this);
                setRight(node.getRight());
                node.getRight().setParent(this);

            }
            this.key = node.getKey();
            this.value = node.value;
        }
    }

    private enum Color {
        RED, BLACK
    }
}
