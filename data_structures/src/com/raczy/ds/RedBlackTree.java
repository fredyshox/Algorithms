package com.raczy.ds;

/**
 * Red black tree implementation. BSTTree with balanced height.
 * Average case complexity of operations (find, insert, delete): O(log(n))
 * Worst case complexity of operations (find, insert, delete): O(log(n))
 */
public class RedBlackTree<T extends Comparable<T>> extends BinarySearchTree<T> {

    /**
     * Represents node's color.
     */
    private enum Color {
        RED, BLACK
    }

    /**
     * Extension of standard BST node adding color field for RB operations.
     * @param <T>
     */
    class ColoredNode<T extends Comparable<T>> extends BinarySearchTree.Node<T> {
        private Color color;

        ColoredNode(Color c, T key) {
            super(key);
            this.color = c;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        /**
         * @return left child or null (excluding sentinel)
         */
        @Override
        public Node<T> getLeft() {
            return (left == RedBlackTree.this.nil) ? null : left;
        }

        /**
         * @return left child (including sentinel)
         */
        public ColoredNode<T> getCLeft() {
            return (ColoredNode<T>) left;
        }

        /**
         * @return right child or null (excluding sentinel)
         */
        @Override
        public Node<T> getRight() {
            return (right == RedBlackTree.this.nil) ? null : right;
        }

        /**
         * @return right child (including sentinel)
         */
        public ColoredNode<T> getCRight() {
            return (ColoredNode<T>) right;
        }

        /**
         * @return parent or null (excluding sentinel)
         */
        @Override
        public Node<T> getParent() {
            return (parent == RedBlackTree.this.nil) ? null : parent;
        }

        /**
         * @return parent (including sentinel)
         */
        public ColoredNode<T> getCParent() {
            return (ColoredNode<T>) parent;
        }
    }

    // black sentinel
    private ColoredNode<T> nil = new ColoredNode<T>(Color.BLACK, null);

    private ColoredNode<T> coloredRoot() {
        return (ColoredNode<T>) this.root;
    }

    /**
     * Similar to standard bst insert but with insertFixup call at the end.
     * @param element value to be inserted
     */
    @Override
    public void insert(T element) {
        // Create a reference to root & initialize a node to nil
        ColoredNode<T> parent = nil;
        ColoredNode<T> current = coloredRoot();
        // inserted node colored RED
        ColoredNode<T> newNone = new ColoredNode<>(Color.RED, element);

        // try to figure out where to put newNode, until tree end is reached
        if (current != null) {
            while (current != nil) {
                parent = current;
                // key is < that current, go left
                if (element.compareTo(current.getKey()) < 0) {
                    current = current.getCLeft();
                }
                // key is > that current, go right
                else {
                    current = current.getCRight();
                }
            }
        }

        newNone.setParent(parent);

        // depending on the value of parent, put newNode as left or right child
        if (parent == nil) {
            root = newNone;
        } else if (element.compareTo(parent.getKey()) < 0) {
            parent.setLeft(newNone);
        } else {
            parent.setRight(newNone);
        }

        // set newNode's children to sentinel
        newNone.setLeft(nil);
        newNone.setRight(nil);
        // fix color properties
        this.insertFixup(newNone);
    }

    /**
     * Inserted node may have caused a violation of RBTree properties. This method fixes possible violations.
     * @param node node that was inserted
     */
    private void insertFixup(ColoredNode<T> node) {
        ColoredNode<T> uncle;
        // while properties are violated
        while (node.getCParent().getColor() == Color.RED) {
            // node parent is left child of its parent
            if (node.getCParent() == node.getCParent().getCParent().getCLeft()) {
                // init variable to be uncle of node
                uncle = node.getCParent().getCParent().getCRight();
                // case 1: uncle is red
                // inserted node is red, so fix this by coloring parent and uncle black, grandparent red
                if (uncle.getColor() == Color.RED) {
                    node.getCParent().setColor(Color.BLACK);
                    uncle.setColor(Color.BLACK);
                    node = node.getCParent().getCParent();
                    node.setColor(Color.RED);
                } else {
                    // case 2: uncle is black & node is right child
                    // transform case 2 into case 3
                    if (node == node.getCParent().getCRight()) {
                        node = node.getCParent();
                        this.leftRotate(node);
                    }
                    // case 3: uncle is black & node is left child
                    node.getCParent().setColor(Color.BLACK);
                    node.getCParent().getCParent().setColor(Color.RED);
                    this.rightRotate(node.getCParent().getCParent());
                    // cases 2 & 3 end while loop
                }
            } else {
                uncle = node.getCParent().getCParent().getCLeft();
                if (uncle.getColor() == Color.RED) {
                    // case 1
                    node.getCParent().setColor(Color.BLACK);
                    uncle.setColor(Color.BLACK);
                    node = node.getCParent().getCParent();
                    node.setColor(Color.RED);
                } else {
                    // case 2
                    if (node == node.getCParent().getCLeft()) {
                        node = node.getCParent();
                        this.rightRotate(node);
                    }
                    // case 3
                    node.getCParent().setColor(Color.BLACK);
                    node.getCParent().getCParent().setColor(Color.RED);
                    this.leftRotate(node.getCParent().getCParent());
                }
            }
        }
        // color root - root is always black
        coloredRoot().setColor(Color.BLACK);
    }

    /**
     * Similar to standard bst delete, but with sentinel instead of nulls and fixup at the end.
     * @param element value to be deleted
     */
    @Override
    public void delete(T element) {
        ColoredNode<T> toDelete = (ColoredNode<T>) findNode(element);
        if (toDelete == null) {
            return;
        }
        ColoredNode<T> temp;
        ColoredNode<T> child;

        if (toDelete.getCLeft() == nil || toDelete.getCRight() == nil) {
            temp = toDelete;
        } else {
            temp = (ColoredNode<T>) this.successorNode(toDelete);
        }

        if (temp.getCLeft() != nil) {
            child = temp.getCLeft();
        } else {
            child = temp.getCRight();
        }

        child.setParent(temp.getCParent());

        if (temp.getCParent() == nil) {
            root = child;
        } else if (temp.getCParent().getCLeft() != nil && temp == temp.getCParent().getCLeft()) {
            temp.getCParent().setLeft(child);
        } else if (temp.getCParent().getCRight() != nil && temp == temp.getCParent().getCRight()){
            temp.getCParent().setRight(child);
        }

        if (temp != toDelete) {
            toDelete.setKey(temp.getKey());
        }

        // red node does not cause violation - does not change black height,
        // does not cause red nodes to be adjacent
        if (temp.getColor() == Color.BLACK) {
            this.deleteFixup(child);
        }
    }

    /**
     * Deleted node may have violated tree's properties. This method fixes possible violations.
     * @param node
     */
    private void deleteFixup(ColoredNode<T> node) {
        ColoredNode<T> sibling;
        // while properties are violated...
        while (node != root && node.getColor() == Color.BLACK) {
            // node is left child
            if (node == node.getCParent().getCLeft()) {
                // init variable to node's sibling
                sibling = node.getCParent().getCRight();
                // case 1: sibling is red
                // transforms to case 2/3/4
                if (sibling.getColor() == Color.RED) {
                    sibling.setColor(Color.BLACK);
                    node.getCParent().setColor(Color.RED);
                    this.leftRotate(node.getCParent());
                    sibling = node.getCParent().getCRight(); // still black
                }
                // case 2: sibling is black & both children of sibling is black
                if (sibling.getCLeft().getColor() == Color.BLACK && sibling.getCRight().getColor() == Color.BLACK) {
                    sibling.setColor(Color.RED);
                    node = node.getCParent(); // repeat loop as node's parent
                } else {
                    // case 3: sibling is black & sibling's right child is black, left is red
                    // transform to case 4
                    if (sibling.getCRight().getColor() == Color.BLACK) {
                        sibling.getCLeft().setColor(Color.BLACK);
                        sibling.setColor(Color.RED);
                        this.rightRotate(sibling);
                        sibling = node.getCParent().getCRight();
                    }
                    // case 4: sibling is black & its right child is red
                    sibling.setColor(node.getCParent().getColor());
                    node.getCParent().setColor(Color.BLACK);
                    sibling.getCRight().setColor(Color.BLACK);
                    this.leftRotate(node.getCParent());
                    node = coloredRoot();
                    // terminates loop
                }
            } else {
                sibling = node.getCParent().getCLeft();
                // case 1
                if (sibling.getColor() == Color.RED) {
                    sibling.setColor(Color.BLACK);
                    node.getCParent().setColor(Color.RED);
                    this.rightRotate(node.getCParent());
                    sibling = node.getCParent().getCLeft();
                }

                if (sibling.getCRight().getColor() == Color.BLACK && sibling.getCLeft().getColor() == Color.BLACK) {
                    // case 2
                    sibling.setColor(Color.RED);
                    node = node.getCParent();
                } else {
                    if (sibling.getCLeft().getColor() == Color.BLACK) {
                        // case 3
                        sibling.getCRight().setColor(Color.BLACK);
                        sibling.setColor(Color.RED);
                        this.leftRotate(sibling);
                        sibling = node.getCParent().getCLeft();
                    }
                    // case 4
                    sibling.setColor(node.getCParent().getColor());
                    node.getCParent().setColor(Color.BLACK);
                    sibling.getCLeft().setColor(Color.BLACK);
                    this.rightRotate(node.getCParent());
                    node = coloredRoot();
                }
            }
        }
        node.setColor(Color.BLACK);
    }

    // Rotations
    // rightRotate(x) | leftRotate(x)
    //      y           x
    //    /   \  <==  /   \
    //   x     c ==> a     y
    //  / \               / \
    // a   b             b   c
    // order: a < x < b < y < c

    private void leftRotate(ColoredNode<T> node) {
        ColoredNode<T> right = node.getCRight();
        node.setRight(right.getCLeft());
        if (right.getCLeft() != nil) {
            right.getCLeft().setParent(node);
        }
        right.setParent(node.getCParent());

        if (node.getCParent() == nil) {
            root = right;
        } else if (node == node.getCParent().getCLeft()) {
            node.getCParent().setLeft(right);
        } else {
            node.getCParent().setRight(right);
        }

        right.setLeft(node);
        node.setParent(right);
    }

    private void rightRotate(ColoredNode<T> node) {
        ColoredNode<T> left = node.getCLeft();
        node.setLeft(left.getCRight());
        if (left.getCRight() != nil) {
            left.getCRight().setParent(node);
        }
        left.setParent(node.getCParent());

        if (node.getCParent() == nil) {
            root = left;
        } else if (node == node.getCParent().getCLeft()) {
            node.getCParent().setLeft(left);
        } else {
            node.getCParent().setRight(left);
        }

        left.setRight(node);
        node.setParent(left);
    }
}
