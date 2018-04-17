package tree;

import java.util.*;

public class SimpleRedBlackTreeOld<K, V> {
    private Comparator<K> comparator;
    private Node root;
    private int size = 0;
    private Set<SimpleRedBlackTreeOld.Entry<K, V>> entrySet = new HashSet<Entry<K, V>>();


    public SimpleRedBlackTreeOld() {}

    public SimpleRedBlackTreeOld(Comparator comparator) {
        this.comparator = comparator;
    }

    public void put(K key, V value) {
        if (key == null)
            throw new NullPointerException();
        Node node = find(key);
        Node newNode = new Node(key, value, new Node(), new Node());
        if (node == null) {
            root = newNode;
            root.left.parent = root;
            root.right.parent = root;
            root.color = Color.BLACK;
            implSize();
        } else if (node.isLeaf()) {
            if (node.parent.left == node) {
                node.parent.left = newNode;
            } else {
                node.parent.right = newNode;
            }
            newNode.left.parent = newNode;
            newNode.right.parent = newNode;
            newNode.parent = node.parent;
            implSize();
        } else {
            node.value = value;
        }
        fixAdd(newNode);
        entrySet.add(new Entry(key, value));
    }

    public V get(K key) {
        return (V) find(key).value;
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

    public boolean removeFirst(K key) {
        Node node = find(key);
        boolean nodeIsRoot = node == root;
        boolean left = false, right = false;
        if (node == null || node.isLeaf())
            return false;
        int leftChildrens = countOfChildrens(node.getLeft());
        int rightChildrens = countOfChildrens(node.getRight());
        if (node == root) {
            if (leftChildrens > rightChildrens) {
                rotateRight(node);
                right = true;
            } else {
                rotateLeft(node);
                left = true;
            }
        }

        Node parentToNode = node.getParent();

        if (leftChildrens > rightChildrens) {
            Node min = min(node.getRight().isLeaf() ? node.getParent() : node.getRight());
            min.left = node.getLeft().getRight();
            if (node.getLeft() != null && !node.getLeft().isLeaf()) {
                node.getLeft().getRight().parent = min;
                node.getLeft().right = node.getRight();
                node.getLeft().parent = node.getParent();
            }
            if (parentToNode.getLeft() == node)
                parentToNode.left = node.getLeft();
            else parentToNode.right = node.getLeft();
            node.getRight().parent = node.getLeft();

        } else {
            Node max = max(node.getLeft().isLeaf() ? node.getParent() : node.getLeft());
            max.right = node.getRight().getLeft();
            max.right = node.getRight().getLeft();
            node.getRight().getLeft().parent = max;
            node.getRight().left = node.getLeft();
            if (parentToNode.getLeft() == node)
                parentToNode.left = node.getRight();
            else
                parentToNode.right = node.getRight();
            node.getRight().parent = node.getParent();
            node.getLeft().parent = node.getRight();
        }
        if (nodeIsRoot) {
            if (right)
                rotateRight(root);
            if (left)
                rotateLeft(root);
        }
        size--;
        if (node.color == Color.BLACK)
            fixRemove(node);
        removeSetNode(key);
        return true;
    }

    private void leftRotate(Node node) {
        Node nodeParent = node.getParent();
        Node nodeRight = node.getRight();
        if(!nodeParent.isLeaf()) {
            if(nodeParent.getLeft() == node)
                nodeParent.left = nodeRight;
            else
                nodeParent.right = nodeRight;
        }
        else {
            root = nodeRight;
            root.parent = new Node();
        }
        node.right = nodeRight.getLeft();
        nodeRight.left = node;
    }

    private void rightRotate(Node node) {
        Node nodeParent = node.getParent();
        Node nodeLeft = node.getLeft();
        if(!nodeParent.isLeaf()) {
            if(nodeParent.getLeft() == node)
                nodeParent.left = nodeLeft;
            else
                nodeParent.right = nodeLeft;
        }
        else {
            root = nodeLeft;
            root.parent = new Node();
        }
        node.left = nodeLeft.getRight();
        nodeLeft.right = node;
    }

    private void fixAfterRemove(Node node) {
        if (node.left.isLeaf() && node.right.isLeaf()) {
//            System.out.println("First --- Key: " + node.key + ", Value: " + node.value);
            node.color = Color.RED;
            return;
        } else {
            if (node.left.isLeaf()) {
//                System.out.println("Fixing 2");
                node.color = Color.RED;
            } else {
//                System.out.println("Fixing 3");
//                System.out.println("Third --- Key: " + node.key + ", Value: " + node.value);
                fixAfterRemove(node.left);
            }
            if (node.right.isLeaf()) {
//                System.out.println("Fourth --- Key: " + node.key + ", Value: " + node.value);
                node.color = Color.RED;
            } else {
                fixAfterRemove(node.right);
            }
        }
    }

    public boolean remove(K key) {
        Node node = find(key);
        Node next = new Node();
        Node tmp = new Node();
        if (node == null || node.isLeaf())
            return false;
        if (node.isLeftFree() && node.isRightFree()) {
            next = node;
        } else {
            next = node.getNext();
        }
        if (!next.isLeftFree())
            tmp = next.getLeft();
        else
            tmp = next.getRight();
        tmp.parent = next.getParent();

        if (next.parent == null || next.parent.isLeaf()) {
            root = tmp;
        } else if (next == next.getParent().getLeft()) {
            next.getParent().left = tmp;
        } else
            next.getParent().right = tmp;

        if (next != node) {
            node.value = next.value;
            node.key = next.key;
        }
        if (next.color == Color.BLACK) {
            fixAdd(tmp);
        }
        size--;
        removeSetNode(key);
        fixRemove(tmp);
        return true;
    }

    private void fixRemove(Node node) {
        Node tmp;
        while (node != root && node.isBlack()) {
            if (node == node.getParent().getLeft()) {
                tmp = node.getParent().getRight();
                if (tmp.isRed()) {
                    tmp.color = Color.BLACK;
                    node.getParent().color = Color.RED;
                    rotateLeft(node.getParent());
                    tmp = node.getParent().getRight();
                }
                if (tmp.getLeft().isBlack() && tmp.getRight().isBlack()) {
                    tmp.color = Color.RED;
                    node = node.getParent();
                } else {
                    if (tmp.getRight().isBlack()) {
                        tmp.getLeft().color = Color.BLACK;
                        tmp.color = Color.RED;
                        rotateRight(tmp);
                        tmp = node.getParent().getRight();
                    }
                    tmp.color = node.getParent().color;
                    node.getParent().color = Color.BLACK;
                    tmp.getRight().color = Color.BLACK;
                    rotateLeft(node.getParent());
                    node = root;
                }
            } else {
                tmp = node.getParent().getLeft();
                if (tmp.isRed()) {
                    tmp.color = Color.BLACK;
                    node.getParent().color = Color.RED;
                    rotateRight(node.getParent());
                    tmp = node.getParent().getLeft();
                }
                if (tmp.getLeft().isBlack() && tmp.getRight().isBlack()) {
                    tmp.color = Color.RED;
                    node = node.getParent();
                } else {
                    if (tmp.getLeft().isBlack()) {
                        tmp.getRight().color = Color.BLACK;
                        tmp.color = Color.RED;
                        rotateLeft(tmp);
                        tmp = node.getParent().getLeft();
                    }
                    tmp.color = node.getParent().color;
                    node.getParent().color = Color.BLACK;
                    tmp.getLeft().color = Color.BLACK;
                    rotateRight(node.getParent());
                    node = root;
                }
            }
        }
        node.color = Color.BLACK;
    }

    public void showAll() {
        Node first = min(root);
        System.out.println(first.key);
        while (true) {
            first = first.getNext();
            if (first.isLeaf())
                break;
            System.out.println(first.key);
        }
    }

    public boolean removeSecond(K key) {
        Node node = find(key);
        boolean nodeIsBlack = node.isBlack();
        if (node == root && size == 1) {
            size--;
            root = null;
            return true;
        }
        Node fixNode = null;
        if (node == null || node.isLeaf())
            return false;
        int leftChildrens = countOfChildrens(node.getLeft());
        int rightChildrens = countOfChildrens(node.getRight());
//        if (node == root) {
//            if (leftChildrens > rightChildrens) {
//                rotateRight(node);
//            } else {
//                rotateLeft(node);
//            }
//        }
        if (node == root) {
            Node temp = null;
            if (leftChildrens > rightChildrens) {
                temp = max(node.getLeft());
            } else
                temp = min(node.getRight());
            if (temp.parent == root) {
                temp.parent = new Node();
                root = temp;
            } else {
                if (leftChildrens > rightChildrens) {
                    fixNode = min(temp.left) == null || min(temp.left).isLeaf() ? temp.parent : null;
                    max(temp.getParent().getLeft()).right = temp.getLeft();
                    temp.parent.right = new Node();
                    root.getLeft().parent = temp;
                    temp.left = root.left;
                    temp.right = root.right;
                    root.getRight().parent = temp;
                    root = temp;
                } else {
                    fixNode = max(temp.right) == null || max(temp.right).isLeaf() ? temp.parent : null;
                    min(temp.getParent().getRight()).left = temp.getRight();
                    temp.parent.left = new Node();
                    root.getRight().parent = temp;
                    temp.left = root.left;
                    temp.right = root.right;
                    root.getRight().parent = temp;
                    root = temp;
                }
            }
        } else {
            if (leftChildrens > rightChildrens) {
                Node max = max(node.getLeft());
                fixNode = max.parent != null && !max.parent.isLeaf() ? max.parent : null;
                if (max == node.getLeft()) {
                    max.right = node.getRight();
                    max.getRight().parent = max;
                    if (node.getParent().getRight() == node)
                        node.getParent().right = max;
                    else
                        node.getParent().left = max;
                    max.parent = node.getParent();
                } else {
                    node.getLeft().parent = max;
                    if (node.getLeft().getRight() == max)
                        node.getLeft().right = new Node();
                    max.right = node.getRight();
                    max.getRight().parent = max;
                    if (node.getParent().getRight() == node)
                        node.getParent().right = max;
                    else
                        node.getParent().left = max;
                    max.parent = node.getParent();
                    max.left = node.left;
                }
            } else {
                Node min = min(node.getRight());
                fixNode = min.parent != null && !min.parent.isLeaf() ? min.parent : null;
                if (min == node.getRight()) {
                    min.left = node.getLeft();
                    min.getLeft().parent = min;
                    if (node.getParent().getRight() == node)
                        node.getParent().right = min;
                    else
                        node.getParent().left = min;
                    min.parent = node.getParent();
                } else {
                    int x;
                    node.getLeft().parent = min;
                    if (node.getRight().getLeft() == min)
                        node.getRight().left = new Node();
                    min.right = node.getRight();
                    min.getRight().parent = min;
                    if (node.getParent().getRight() == node)
                        node.getParent().right = min;
                    else
                        node.getParent().left = min;
                    min.parent = node.getParent();
                    min.left = node.left;
                }
            }
        }
        size--;
        removeSetNode(key);
//        fixAfterRemove(root);
        if (fixNode != null && !fixNode.isLeaf() && nodeIsBlack)
            fixRemove(fixNode);
        return true;
    }

    public boolean removeThird(K key) {
        Node node = find(key);
        Node tmp = null;
        Node fixNode = null;
        if (node == null || node.isLeaf())
            return false;
        if (countOfChildrens(node.getLeft()) > countOfChildrens(node.getRight())) {
            tmp = node.getLeft();
//            while (node.left != null && !node.getLeft().isLeaf()) {
            do {
                rotateRight(node);
            }
            while (!node.getParent().isLeaf());
            fixNode = node.getParent();
            node.getParent().right = null;
        } else {
            tmp = node.getRight();
//            while (node.right != null && !node.getRight().isLeaf()) {
            do {
                rotateLeft(node);
            }
            while (node.getParent().isLeaf());
            fixNode = node.getParent();
            node.getParent().left = null;
        }
//        if (node == root && tmp != null) {
//            root = tmp;
//        }
        if (fixNode != null) {
            fixNode.color = Color.RED;
            fixAdd(fixNode);
        }
        size--;
        return true;
    }

    private void removeSetNode(K key) {
        Iterator<Entry<K, V>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (entry.getKey().equals(key))
                iterator.remove();
        }
    }

    private int countOfChildrens(Node node) {
        int result = 0;
        if (node == null)
            return result;
        if (node.left != null)
            result = node.left.isLeaf() ? result : result + 1;
        if (node.right != null)
            result = node.right.isLeaf() ? result : result + 1;
        if (result == 0)
            return result;
        else {
            return result + countOfChildrens(node.left) + countOfChildrens(node.right);
        }
    }

    private Node min(Node node) {
        if (node == null || node.isLeaf())
            return node;
        if (node.left == null || node.left.isLeaf())
            return node;
        else return min(node.left);
    }

    private Node max(Node node) {
        if (node == null || node.isLeaf())
            return node;
        if (node.right == null || node.right.isLeaf())
            return node;
        else return max(node.right);
    }

    private void fixAdd(Node addedNode) {
        Node tmp;
        while (addedNode.parent != null && !addedNode.isLeaf() && addedNode.parent.isRed()) {
            if (addedNode.parent == addedNode.getGrandfather().left) {
                tmp = addedNode.getGrandfather().right;
                if (tmp.isRed()) {
                    tmp.color = Color.BLACK;
                    addedNode.parent.color = Color.BLACK;
                    addedNode.getGrandfather().color = Color.RED;
                    addedNode = addedNode.getGrandfather();
                } else {
                    if (addedNode == addedNode.parent.right) {
                        addedNode = addedNode.parent;
                        rotateLeft(addedNode);
                    }
                    addedNode.parent.color = Color.BLACK;
                    addedNode.getGrandfather().color = Color.RED;
                    rotateRight(addedNode.getGrandfather());
                }
            } else {
                tmp = addedNode.getGrandfather().left;
                if (tmp.isRed()) {
                    tmp.color = Color.BLACK;
                    addedNode.parent.color = Color.BLACK;
                    addedNode.getGrandfather().color = Color.RED;
                    addedNode = addedNode.getGrandfather();
                } else {
                    if (addedNode == addedNode.parent.left) {
                        addedNode = addedNode.parent;
                        rotateRight(addedNode);
                    }
                    addedNode.parent.color = Color.BLACK;
                    addedNode.getGrandfather().color = Color.RED;
                    rotateLeft(addedNode.getGrandfather());
                }
            }
        }
        root.color = Color.BLACK;
    }

    private void rotateLeft(Node node) {
        Node tmp = node.right;
        node.right = tmp.left;
        if (tmp != null && !tmp.isLeaf()) {
            tmp.left.parent = node;
            if (node != root) {
                tmp.parent = node.parent;
                if (node == node.parent.left)
                    node.parent.left = tmp;
                else
                    node.parent.right = tmp;
            } else {
                tmp.parent = null;
                root = tmp;
            }
        }
        tmp.left = node;
        tmp.color = Color.BLACK;
        node.parent = tmp;
    }

    private void rotateRight(Node node) {
        Node tmp = node.left;
        node.left = tmp.right;
        if (tmp != null && !tmp.isLeaf()) {
            tmp.getRight().parent = node;
            if (node != root) {
                tmp.parent = node.parent;
                if (node == node.getParent().left)
                    node.getParent().left = tmp;
                else
                    node.getParent().right = tmp;
            } else {
                tmp.parent = null;
                root = tmp;
                tmp.color = Color.BLACK;
            }
        }
        tmp.right = node;
        node.parent = tmp;
    }


    private void implSize() {
        size++;
    }

    public int size() {
        return size;
    }

    public Set<Entry<K, V>> entrySet() {
        return entrySet;
    }


    private class Node<K, V> {
        private Node parent;
        private Node left;
        private Node right;
        private Color color;
        private K key;
        private V value;

        private Node() {
            color = Color.BLACK;
        }

        private Node(K key, V value, Node left, Node right) {
            this.key = key;
            this.value = value;
            color = Color.RED;
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return this == null || key == null;
        }

        private boolean isLeftFree() {
            return (left == null || left.isLeaf());
        }

        private boolean isRightFree() {
            return (right == null || right.isLeaf());
        }

        private boolean isBlack() {
            return color == Color.BLACK;
        }

        private boolean isRed() {
            return color == Color.RED;
        }

        private Node getGrandfather() {
            if (parent != null && !parent.isLeaf()) {
                return parent.parent;
            }
            return null;
        }

        private Node getUncle() {
            Node grandFather = getGrandfather();
            if (grandFather != null) {
                if (grandFather.left != parent)
                    return grandFather.left;
                else if (grandFather.right != parent)
                    return grandFather.right;
            }
            return null;
        }

        private Node getLeaf() {
            Node leaf = new Node();
            leaf.parent = this;
            return leaf;
        }

        private Node getNext() {
            Node result = null;
            Node currentNode = this;
            if (!currentNode.isRightFree()) {
                result = currentNode.getRight();
                while (!result.isLeftFree())
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

        private Node<K, V> getRight() {
            if (right == null)
                right = getLeaf();
            return right;
        }

        private Node<K, V> getLeft() {
            if (left == null)
                left = getLeaf();
            return left;
        }

        private Node<K, V> getParent() {
            if (parent == null)
                parent = new Node();

            return parent;
        }

    }

    public static class Entry<K, V> {
        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            K key1 = this.key;
            return key1.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            K key1 = this.key;
            K key2 = (K) o;
            return key1.toString().equals(key2.toString());
        }

    }

    private enum Color {
        RED, BLACK
    }
}
