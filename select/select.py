#!/usr/bin/env python
#
# select.py
#
# Copyright (c) 2018 Kacper Raczy. All rights reserved.

from random import randrange
import math
import sys

# Data class for statistics

class Stats(object):
    def __init__(self, length=0, kidx=0, ncomp=0, nswap=0, time=0.0):
        self.length = length
        self.kidx = kidx
        self.ncomp = ncomp
        self.nswap = nswap
        self.time = time

    def inc_comp(self, n=1):
        self.ncomp += n

    def inc_swap(self, n=1):
        self.nswap += n

    def __repr__(self):
        return 'Stats(ncomp: {}, nswap: {}, time: {})'.format(self.ncomp, self.nswap, self.time)

# Select

def select(array, nelem, left=0, right=None, stats=None):
    if right is None:
        right = len(array) - 1

    if (right - left + 1) < 10:
        return sorted(array[left: right + 1])[nelem]

    subarrays = [array[i:i+5] for i in range(left, right + 1, 5)]
    medians = [sorted(arr)[len(arr) // 2] for arr in subarrays]

    median = select(medians, len(medians) // 2)
    piv = pivot(array, median, left, right, stats=stats)
    print_err('Pivot {}'.format(piv))
    length = piv - left

    if nelem < length:
        return select(array, nelem, left, piv - 1, stats=stats)
    elif nelem > length:
        return select(array, nelem - length, piv, right, stats=stats)
    else:
        return median

def pivot(array, pivot, left, right, stats=None):
    val = pivot
    position = left

    comp_c = 0
    swap_c = 1
    for j in range(left, right + 1):
        comp_c += 1
        print_err('Comp {} {}'.format(array[j], val))
        if array[j] < val: # array[j] < val
            print_err('Swap {} {}'.format(array[position], array[j]))
            swap(array, position, j)
            position += 1
            swap_c += 1
    print_err('Swap {} {}'.format(array[position], array[right]))
    __update_stats(lambda s: (s.inc_comp(comp_c), s.inc_swap(swap_c)), stats)
    return position


# Randomized Select

def randomized_select(array, nelem=0, left=0, right=None, stats=None):
    if right is None:
        right = len(array) - 1

    if left == right:
        return array[left]

    pivot = randomized_partition(array, left, right, stats)
    print_err('Pivot {}'.format(pivot))
    k = pivot - left + 1

    if nelem == k:
        return array[pivot]
    elif nelem < k:
        return randomized_select(array, nelem, left, pivot - 1, stats)
    else:
        return randomized_select(array, nelem - k, pivot + 1, right, stats)

def randomized_partition(array, left, right, stats=None):
    i = (randrange(left, right) if left != right else left)
    swap(array, right, i)
    return partition(array, left, right, stats=stats)

def partition(array, left, right, stats=None):
    val = array[right]
    position = left

    comp_c = 0
    swap_c = 2
    for j in range(left, right):
        comp_c += 1
        print_err('Comp {} {}'.format(array[j], val))
        if array[j] < val: # array[j] < val
            print_err('Swap {} {}'.format(array[position], array[j]))
            swap(array, position, j)
            position += 1
            swap_c += 1
    print_err('Swap {} {}'.format(array[position], array[right]))
    swap(array, position, right)
    __update_stats(lambda s: (s.inc_comp(comp_c), s.inc_swap(swap_c)), stats)
    return position

# Utility

def print_err(*args, **kwargs):
    print(*args, **kwargs, file=sys.stderr)

def __update_stats(cb, stats):
    if not stats is None:
        cb(stats)

def swap(array, i1, i2):
    temp = array[i1]
    array[i1] = array[i2]
    array[i2] = temp

if __name__ == '__main__':
    arr = [5,22,7,8,9,4]
    print(randomized_select(arr, 1))
