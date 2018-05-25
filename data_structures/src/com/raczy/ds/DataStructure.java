package com.raczy.ds;

/**
 * Defines basic functionality of data structure.
 * @param <T>
 */
public interface DataStructure<T> {
    void insert(T element);
    void delete(T element);
    boolean find(T element);
    T min();
    T max();
    T successor(T element);

    /**
     * Performs in order walk. Prints all values in order.
     */
    void inorder();
}
