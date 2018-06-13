#!/usr/bin/env python
#
# main.py
#
# Copyright (c) 2018 Kacper Raczy. All rights reserved.

from enum import Enum
import sys
import argparse
import time
from sort import *

def test_sort(array, order):
    """ Test array if it's sorted """
    prev = array[0]
    for i in range(1, len(array)):
        if not order.compare(array[i], prev):
            return False
        prev = array[i]
    return True

def print_err(*args, **kwargs):
    """ Print to stderr """
    print(*args, **kwargs, file=sys.stderr)

def parse_int(n):
    try:
        return int(n)
    except ValueError:
        return None

def main():
    parser = argparse.ArgumentParser(description='Array sorting program.')
    required = parser.add_argument_group('Required arguments')
    required.add_argument('--type', help='sorting algorithm', type=str, dest='algorithm', choices=['insert','merge','quick', 'dpquick'])
    required.add_argument('--comp', help='order', type=str, dest='order', choices=['>=', '<='])
    args = parser.parse_args()

    if args.algorithm is None or args.order is None:
        parser.print_usage()
        return 1

    algorithm = list(map(lambda x: Algorithm[x.upper()], [args.algorithm]))[0]
    order = list(map(lambda x: Order.LE if x == '<=' else Order.GE, [args.order]))[0]

    try:
        length = int(input('length: '))
        elements_str = input('array: ')
        elements = list(filter(lambda x: x != '', elements_str.split(' ')))
        elements = list(map(lambda s: int(s), elements))
    except ValueError:
        parser.error('Invalid input')
        return 3

    if len(elements) != length:
        parser.error('Count of elements does not match')
        return 4

    stats = Stats(length)

    start = time.time()
    if algorithm is Algorithm.INSERT:
        insertion_sort(elements, order=order, stats=stats)
    elif algorithm is Algorithm.MERGE:
        merge_sort(elements, order=order, stats=stats)
    elif algorithm is Algorithm.QUICK:
        quicksort(elements, order=order, stats=stats)
    elif algorithm is Algorithm.DPQUICK:
        dual_pivot_quicksort(elements, 0, len(elements) - 1, order=order, stats=stats)
    elif algorithm is Algorithm.RADIX:
        radix_sort(elements, order=order, stats=stats)
    elif algorithm is Algorithm.HYBRID:
        hybrid_sort(array, order=order, stats=stats)
    end = time.time()

    stats.time = end - start
    print_err('Sorting stats: {}'.format(stats))
    print_err('Sorting validation: {}'.format(test_sort(elements, order)))
    print('Sorted array: {}'.format(elements))
    return 0

if __name__ == "__main__":
    main()
