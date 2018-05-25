package com.raczy.ds;

import java.util.ArrayList;

/**
 * Binary search tree implementation.
 * Average case complexity of operations (find, insert, delete): O(log(n))
 * Worst case complexity of operations (find, insert, delete): O(n)
 * @param <T>
 */
public class BinarySearchTree<T extends Comparable<T>> implements DataStructure<T> {

    /**
     * Represents node of BST tree.
     * @param <T>
     */
    static class Node<T> {
        private T key;
        protected Node<T> left = null;
        protected Node<T> right = null;
        protected Node<T> parent = null;

        public Node(T key) {
            this.key = key;
        }

        public T getKey() {
            return key;
        }

        public void setKey(T key) {
            this.key = key;
        }

        public Node<T> getLeft() {
            return left;
        }

        public void setLeft(Node<T> left) {
            this.left = left;
        }

        public Node<T> getRight() {
            return right;
        }

        public void setRight(Node<T> right) {
            this.right = right;
        }

        public Node<T> getParent() {
            return parent;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }
    }

    protected Node<T> root = null;

    // DataStructure interface implementation
    @Override
    public void insert(T element) {
        Node<T> parent = null;
        Node<T> current = root;
        Node<T> newNode = new Node<>(element);

        while (current != null) {
            parent = current;
            if (element.compareTo(current.getKey()) < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        newNode.setParent(parent);
        if (parent == null) {
            this.root = newNode;
        } else if (element.compareTo(parent.getKey()) < 0) {
            parent.setLeft(newNode);
        } else {
            parent.setRight(newNode);
        }
    }

    @Override
    public void delete(T element) {
        Node<T> toDelete = findNode(element);
        if (toDelete == null) {
            return;
        }

        Node<T> temp = null;
        Node<T> child = null;

        if (toDelete.getLeft() == null || toDelete.getRight() == null) {
            temp = toDelete;
        } else {
            temp = successorNode(toDelete);
        }

        if (temp.getLeft() != null) {
            child = temp.getLeft();
        } else {
            child = temp.getRight();
        }

        if (child != null) {
            child.setParent(temp.getParent());
        }

        if (temp.getParent() == null) {
            this.root = child;
        } else {
            if (temp == temp.getParent().getLeft()) {
                temp.getParent().setLeft(child);
            } else {
                temp.getParent().setRight(child);
            }
        }

        if (temp != toDelete) {
            toDelete.setKey(temp.getKey());
        }
    }

    @Override
    public boolean find(T element) {
       Node<T> node = findNode(element);

       return (node != null);
    }

    @Override
    public T min() {
        if (root == null) {
            return null;
        }

        return minNode(root).getKey();
    }

    @Override
    public T max() {
        if (root == null) {
            return null;
        }

        return maxNode(root).getKey();
    }

    @Override
    public T successor(T element) {
        Node<T> node = findNode(element);
        if (node == null) {
            return null;
        }

        return successorNode(node).getKey();
    }

    @Override
    public void inorder() {
        System.out.println(this.toString());
    }

    // Object methods

    @Override
    public String toString() {
        ArrayList<String> list = new ArrayList<>();
        inorderRecursive(root, list);
        return String.join(" - ", list);
    }

    // Helper methods

    protected Node<T> minNode(Node<T> from) {
        Node<T> current = from;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }

        return current;
    }

    protected Node<T> maxNode(Node<T> from) {
        Node<T> current = from;
        while(current.getRight() != null) {
            current = current.getRight();
        }

        return current;
    }

    protected Node<T> findNode(T value) {
        Node<T> current = root;
        while (current != null && value.compareTo(current.getKey()) != 0) {
            if (value.compareTo(current.getKey()) < 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }
        }

        return current;
    }

    protected Node<T> successorNode(Node<T> node) {
        if (node.getRight() != null) {
            return minNode(node.getRight());
        }

        Node<T> current = node.getParent();
        Node<T> temp = node;
        while(current != null && current.getLeft() != temp) {
            temp = current;
            current = current.getParent();
        }

        return current;
    }

    private void inorderRecursive(Node<T> node, ArrayList<String> strings) {
        if (node != null) {
            inorderRecursive(node.getLeft(), strings);
            strings.add(node.getKey().toString());
            inorderRecursive(node.getRight(), strings);
        }
    }

}
