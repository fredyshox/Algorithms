package com.raczy;

import com.raczy.ds.bloom.BloomFilter;
import com.raczy.ds.bloom.HashFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;
import java.util.zip.CRC32;

/**
 * Created by kacperraczy on 09.06.2018.
 */
public class BloomTest {

    static double test1(BufferedReader input, BufferedReader test, int m, int k) {
        BloomFilter<String> bf = new BloomFilter<String>(m, k, HashFactory::hashCRC);
        String s;
        int failures = 0, count = 0;
        try {
            while ((s = input.readLine()) != null) {
                if (s.length() != 0) {
                    bf.insert(s);
                }
            }
            System.out.println(String.format("m = %d, k = %d count = %d", m, k, bf.getInsertedCount()));
            while ((s = test.readLine()) != null) {
                if (s.length() != 0) {
                    if (bf.find(s)) {
                        failures++;
                    }
                    count++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return count != 0 ? (double) failures / count : 0;
    }

    public static void main(String[] args) {
        try {
            FileInputStream inputFis = new FileInputStream("aspell_wordlist2.txt");
            FileInputStream testFis = new FileInputStream("bf_test.txt");
            PrintWriter pw = new PrintWriter(new File("bloom_output.csv"));
            pw.write("m,k,fpRate\n");
            BufferedReader inputBr, testBr;
            int[] mArr = {10_000, 50_000, 100_000, 500_000, 1000_000, 5000_000, 10_000_000};
            int[] kArr = {1, 3, 5, 8};
            double fpRate;
            for (int m : mArr) {
                for (int k : kArr) {
                    inputFis.getChannel().position(0);
                    testFis.getChannel().position(0);
                    inputBr = new BufferedReader(new InputStreamReader(inputFis));
                    testBr = new BufferedReader(new InputStreamReader(testFis));
                    fpRate = test1(inputBr, testBr, m, k);
                    pw.write(String.format(Locale.US, "%d,%d,%.9f\n", m, k, fpRate));

                    System.out.println(String.format("m = %d, k = %d DONE", m, k));
                }
            }
            inputFis.close();
            testFis.close();
            pw.close();
        } catch (Exception e) {
            System.err.println(e);
        }

    }

}
