package com.raczy.ds;

/**
 * Adapts HashTable functionality for DataStructure operations.
 * Used to compare running time (complexity) with other DataStructure compatible structures.
 */
public class HashTableAdapter<T extends Comparable<T>> implements DataStructure<T> {
    private HashTable<T, T> map = new HashTable<>();

    // DataStructure implementation
    @Override
    public void insert(T element) {
        map.add(element, element);
    }

    @Override
    public void delete(T element) {
        map.delete(element);
    }

    @Override
    public boolean find(T element) {
        T value = map.get(element);
        return value != null;
    }

    // Operations not supported
    @Override
    public T min() {
        return null;
    }

    @Override
    public T max() {
        return null;
    }

    @Override
    public T successor(T element) {
        return null;
    }

    @Override
    public void inorder() {
        System.out.println();
    }
}
