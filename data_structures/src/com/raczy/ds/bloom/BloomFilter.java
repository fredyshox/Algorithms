package com.raczy.ds.bloom;

import com.raczy.ds.DataStructure;
import com.raczy.ds.bloom.HashFactory.HashFunction;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.function.Consumer;

/**
 * Implementation of BloomFilter
 * Created by kacperraczy on 09.06.2018.
 */
public class BloomFilter<T> implements DataStructure<T> {

    private int insertedCount = 0;
    private BitSet fields;
    private HashFunction hashFunction;
    private int hashCount;
    private int fieldCount;
    private int expectedCapacity;


    public BloomFilter(int fieldCount, int hashCount, HashFunction function) {
        assert hashCount < fieldCount;
        this.expectedCapacity = -1;
        this.initialize(fieldCount, hashCount, function);
    }

    public BloomFilter(int expectedCapacity, double fpRate) {
        int m = (int) Math.ceil((-1 * expectedCapacity * Math.log(fpRate)) / Math.pow(Math.log(2), 2));
        int k = (int) Math.ceil((m / expectedCapacity) * Math.log(2));
        this.expectedCapacity = expectedCapacity;
        this.initialize(m, k, HashFactory::hashCRC);
    }

    private void initialize(int fieldCount, int hashCount, HashFunction function) {
        this.fields = new BitSet(fieldCount);
        this.hashCount = hashCount;
        this.fieldCount = fieldCount;
        this.hashFunction = function;
    }

    // MARK: DataStructure

    @Override
    public void insert(T element) {
        // increment field values chosen by hash func
        hash(element, (index) -> {
            fields.set(index, true);
        });
        insertedCount += 1;
    }

    @Override
    public boolean find(T element) {
        // gather field values
        ArrayList<Boolean> fieldValues = new ArrayList<>();
        hash(element, (index) -> {
            fieldValues.add(fields.get(index));
        });

        // test if all field values are non-zero
        return fieldValues.stream().allMatch(v -> v);
    }

    // For counting bloom filter
    //    @Override
    //    public void delete(T element) {
    //        // decrement field values chosen by hash func
    //        hash(element, (index) -> {
    //            if (fields[index] > 0) {
    //                fields[index] -= 1;
    //            }
    //        });
    //    }

    /**
     * Hash element using hashFunctions
     * Executes action for each result of hash function
     * @param element hashed element
     * @param action executed for each hash
     */
    private void hash(T element, Consumer<Integer> action) {
        int[] values = hashFunction.hash(element.toString().getBytes(), fieldCount, hashCount);
        for(int val : values) {
            action.accept(val);
        }
    }

    // MARK: Accessors

    public double getFalsePositiveProbability() {
        return Math.pow(1 - Math.exp(-hashCount * expectedCapacity / fieldCount), hashCount);
    }

    public int getHashCount() {
        return hashCount;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public BitSet getFields() {
        return fields;
    }

    public int getInsertedCount() {
        return insertedCount;
    }

    public int getExpectedCapacity() {
        return expectedCapacity;
    }

    public HashFunction getHashFunction() {
        return hashFunction;
    }

    public int fillLevel() {
        int result = 0;
        for (int i = 0; i < fields.length(); i++) {
            if (fields.get(i)) {
                result += 1;
            }
        }

        return result;
    }

    // MARK: Unsupported operations
    @Override
    public void delete(T element) {}

    @Override
    public T min() {
        return null;
    }

    @Override
    public T max() {
        return null;
    }

    @Override
    public void inorder() {
        System.out.println();
    }

    @Override
    public T successor(T element) {
        return null;
    }
}
