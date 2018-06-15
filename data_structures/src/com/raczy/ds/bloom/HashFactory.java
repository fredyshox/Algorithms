package com.raczy.ds.bloom;

import java.util.zip.CRC32;

/**
 * Set of hashing functions useful in BloomFilter
 * Created by kacperraczy on 09.06.2018.
 */
public class HashFactory {
    public interface HashFunction {
        int[] hash(byte[] value, int m, int k);
    }

    public static int[] hashCRC(byte[] value, int m, int k) {
        int[] positions = new int[k];
        int count = 0;
        CRC32 crc = new CRC32();
        while (count < k) {
            crc.reset();
            crc.update(value);

            crc.update(count + 49979089);
            positions[count] = Math.abs((int) crc.getValue() % m);
            count++;
        }

        return positions;
    }
}