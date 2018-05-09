#!/usr/bin/env python
#
# test.py
#
# Copyright (c) 2018 Kacper Raczy. All rights reserved.

import random
import time
import csv
from bsearch import binary_search, Stats

def analyze(bs_func, array, nelem):
    stats = Stats(len(array), nelem)
    start = time.time()
    bs_func(array, nelem, stats=stats)
    end = time.time()
    stats.time = end - start

    return stats

def test(output, idx=None):
    results = []
    for n in range(1000, 101000, 1000):
        for _ in range(100):
            arr = list(range(n))
            val = random.randrange(n) if idx is None else idx
            stats = analyze(binary_search, arr, val)
            results.append(vars(stats))
            del arr, val
    with open(output, 'w') as f:
        writer = csv.DictWriter(f, fieldnames=vars(Stats()).keys())
        writer.writeheader()
        for row in results:
            writer.writerow(row)

if __name__ == '__main__':
    test('output.csv')
