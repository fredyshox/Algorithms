package com.raczy.ds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Hash table with chaining implementations.
 * Average case complexity of operations (find, insert, delete): O(1)
 * Worst case complexity of operations (find, insert, delete): O(n)
 */
public class HashTable<K, V extends Comparable<V>> {

    class HashNode<K, V extends Comparable<V>> {
        private K key;
        private V value;

        // Reference to next
        HashNode<K, V> next;

        // Init
        HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        // Accessors
        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public HashNode<K, V> getNext() {
            return next;
        }

        public void setNext(HashNode<K, V> next) {
            this.next = next;
        }
    }

    // Constants
    public static int DEFAULT_SIZE = 100;

    // Config properties
    // number of buckets
    private int size;

    // Functions
    private BiFunction<K, Integer, Integer> hashFunction;

    // Storage
    private List<HashNode<K, V>> storage;

    // Initialization
    public HashTable() {
        this.size = DEFAULT_SIZE;
        this.hashFunction = getDefaultHashFunction();
        this.storage = new ArrayList<>(Collections.nCopies(size, null));
    }

    public HashTable(int size, int bound, BiFunction<K, Integer, Integer> hashFunction) {
        this.size = size;
        this.hashFunction = hashFunction;
        this.storage = new ArrayList<>(Collections.nCopies(size, null));
    }

    // Accessors
    public int getSize() {
        return size;
    }

    public BiFunction<K, Integer, Integer> getHashFunction() {
        return hashFunction;
    }

    // Methods
    public void add(K key, V value) {
        // find chain head for given key
        int hash = this.hashFunction.apply(key, this.size);
        HashNode<K, V> head = storage.get(hash);

        // check if node already exists
        while(head != null) {
            if (head.getKey().equals(key)) {
                // node exist for given key, update its value
                head.setValue(value);
                return;
            }
            head = head.getNext();
        }
        // add new node with given key/value
        head = storage.get(hash);
        HashNode<K, V> node = new HashNode<>(key, value);
        node.setNext(head);
        storage.set(hash, node);
    }

    public void delete(K key) {
        // find chain head for given key
        int hash = this.hashFunction.apply(key, this.size);
        HashNode<K, V> head = storage.get(hash);
        HashNode<K, V> prev = null;

        // search for key, storing reference to previous node
        while (head != null) {
            if (head.getKey().equals(key)) {
                if (prev == null) {
                    storage.set(hash, head.next);
                } else {
                    prev.setNext(head.next);
                }
                return;
            }
            prev = head;
            head = head.getNext();
        }
    }

    public V get(K key) {
        // find chain head for given hey
        int hash = this.hashFunction.apply(key, size);
        HashNode<K, V> head = storage.get(hash);

        // search for key
        while(head != null) {
            if (head.getKey().equals(key)) {
                return head.getValue();
            }
            head = head.getNext();
        }
        // null if key doesn't exist
        return null;
    }

    // utility

    /**
     * Default hash function. (hashCode absolute value) mod (nBuckets).
     */
    BiFunction<K, Integer, Integer> getDefaultHashFunction() {
        return (obj, m) -> Math.abs(obj.hashCode()% m);
    }
}
