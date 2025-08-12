package implementations;

import utilities.BSTreeADT;
import utilities.Iterator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable {
    private static final long serialVersionUID = 1L;

    private BSTreeNode<E> root;
    private int size;

    public BSTree() {
        root = null;
        size = 0;
    }

    public BSTree(E element) {
        if (element == null) throw new NullPointerException("element is null");
        root = new BSTreeNode<>(element);
        size = 1;
    }


    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException {
        if (root == null) throw new NullPointerException("Tree is empty");
        return root;
    }

    @Override
    public int getHeight() {
        return getHeightHelper(root);
    }

    private int getHeightHelper(BSTreeNode<E> node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getHeightHelper(node.getLeft()), getHeightHelper(node.getRight()));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean contains(E entry) throws NullPointerException {
        if (entry == null) throw new NullPointerException("Entry is null");
        return search(entry) != null;
    }

    @Override
    public BSTreeNode<E> search(E entry) throws NullPointerException {
        if (entry == null) throw new NullPointerException("Node is empty");
        return searchHelper(root, entry);
    }
    private BSTreeNode<E> searchHelper(BSTreeNode<E> node, E entry) {
        if (node == null) return null;
        int comparedInt = entry.compareTo(node.getData());
        if (comparedInt == 0) return node;
        else if (comparedInt < 0) return searchHelper(node.getLeft(), entry);
        else return searchHelper(node.getRight(), entry);
    }

    @Override
    public boolean add(E newEntry) throws NullPointerException {
        if  (newEntry == null) throw new NullPointerException("Entry is null");
        if (root == null) {
            root = new BSTreeNode<>(newEntry);
            size++;
            return true;
        }
        return addHelper(root, newEntry);
    }
    private boolean addHelper(BSTreeNode<E> node, E newEntry) {
        int compareInt = newEntry.compareTo(node.getData());
        if (compareInt == 0) {
            return false; //this will be able to stop a duplicate entry from being added
        }
        else if (compareInt < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new BSTreeNode<>(newEntry));
                size++;
                return true;
            }
            else {
                return addHelper(node.getLeft(), newEntry);
            }
        }
        else {
            if (node.getRight() == null) {
                node.setRight(new BSTreeNode<>(newEntry));
                size++;
                return true;
            }
            else {
                return addHelper(node.getRight(), newEntry);
            }
        }
    }

    @Override
    public BSTreeNode<E> removeMin() {
        if (root == null) return null;
        BSTreeNode<E> minNode = findMin(root);
        root = removeMinHelper(root);
        size--;
        return minNode;
    }
    private BSTreeNode<E> findMin(BSTreeNode<E> node) {
        while (node.getLeft() != null) node = node.getLeft();
        return node;
    }
    private BSTreeNode<E> removeMinHelper(BSTreeNode<E> node) {
        if (node.getLeft() == null) return node.getRight();
        node.setLeft(removeMinHelper(node.getLeft()));
        return node;
    }

    @Override
    public BSTreeNode<E> removeMax() {
        if (root == null) return null;
        BSTreeNode<E> maxNode = findMax(root);
        root = removeMaxHelper(root);
        size--;
        return maxNode;
    }
    private BSTreeNode<E> findMax(BSTreeNode<E> node) {
        while (node.getRight() != null) node = node.getRight();
        return node;
    }
    private BSTreeNode<E> removeMaxHelper(BSTreeNode<E> node) {
        if (node.getRight() == null) return node.getLeft();
        node.setRight(removeMaxHelper(node.getRight()));
        return node;
    }

    @Override
    public Iterator<E> inorderIterator() {
        ArrayList<E> list = new ArrayList<>();
        inorderedTraversal(root, list);
        return new BSTreeIterator<>(list);
    }
    private void inorderedTraversal(BSTreeNode<E> node, ArrayList<E> list) {
        if (node != null) {
            inorderedTraversal(node.getLeft(), list);
            list.add(node.getData());
            inorderedTraversal(node.getRight(), list);
        }
    }

    @Override
    public Iterator<E> preorderIterator() {
        ArrayList<E> list = new ArrayList<>();
        preorderTraversal(root, list);
        return new BSTreeIterator<>(list);
    }
    private void preorderTraversal(BSTreeNode<E> node, ArrayList<E> list) {
        if (node != null) {
            list.add(node.getData());
            preorderTraversal(node.getLeft(), list);
            preorderTraversal(node.getRight(), list);
        }
    }

    @Override
    public Iterator<E> postorderIterator() {
        ArrayList<E> list = new ArrayList<>();
        postorderTraversal(root, list);
        return new BSTreeIterator<>(list);
    }
    private void postorderTraversal(BSTreeNode<E> node, ArrayList<E> list) {
        if (node != null) {
            postorderTraversal(node.getLeft(), list);
            postorderTraversal(node.getRight(), list);
            list.add(node.getData());
        }
    }

    private class BSTreeIterator<E> implements Iterator<E> {
        private ArrayList<E> elements;
        private int index = 0;

        public BSTreeIterator(ArrayList<E> list) {
            this.elements = new ArrayList<>(list);
        }

        @Override
        public boolean hasNext() {
            return index < elements.size();
        }

        @Override
        public E next() throws NoSuchElementException {
            if (!hasNext()) throw new NoSuchElementException();
            return elements.get(index++);
        }
    }
}
