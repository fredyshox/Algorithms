#!/usr/bin/env python
#
# sort.py
#
# Copyright (c) 2018 Kacper Raczy. All rights reserved.

from math import floor
from enum import Enum

class Algorithm(Enum):
    INSERT = 0
    MERGE = 1
    QUICK = 2
    DPQUICK = 3

class Stats(object):
    """ Represents sorting statistics. """

    def __init__(self, length=0, ncomp=0, nswap=0, time=0.0):
        self.length = length
        self.ncomp = ncomp
        self.nswap = nswap
        self.time = time

    def inc_comp(self, n=1):
        self.ncomp += n

    def inc_swap(self, n=1):
        self.nswap += n

    def __repr__(self):
        return 'Stats(ncomp: {}, nswap: {}, time: {})'.format(self.ncomp, self.nswap, self.time)

class Order(Enum):
    """ Represents sorting order.
        LE - Less or Equal, GE - Greater or Equal
    """

    LE = 0
    GE = 1

    def compare(self, a, b, eq=True):
        if self is Order.LE:
            if eq:
                return a >= b
            else:
                return a > b
        else:
            if eq:
                return a <= b
            else:
                return a < b

# Insertion Sort

def insertion_sort(array, order=Order.LE, stats=None):
    comp_c = 0
    swap_c = 0
    for i in range(1, len(array)):
        key = array[i]
        j = i
        while j > 0:
            comp_c += 1
            if order.compare(array[j - 1], key): # array[j-1] > key
                array[j] = array[j - 1]
                j = j - 1
                swap_c += 1
            else:
                break
        array[j] = key
    __update_stats(lambda s: (s.inc_swap(swap_c), s.inc_comp(comp_c)), stats)

# Merge Sort

def merge_sort(array, p=0, r=None, order=Order.LE, stats=None):
    if r is None:
        r = len(array) - 1
    if p < r:
        q = int(floor((p + r) / 2))
        merge_sort(array, p, q, order, stats)
        merge_sort(array, q + 1, r, order, stats)
        merge(array, p, q, r, order, stats)

def merge(array, p, q, r, order, stats=None):
    n1 = q - p + 1
    n2 = r - q
    left = [0] * n1
    right = [0] * n2
    for i in range(0, n1):
        left[i] = array[p + i]
    for j in range(0, n2):
        right[j] = array[q + j + 1]
    k = p
    comp_c = 0
    swap_c = 0
    while len(left) != 0 and len(right) != 0 and k <= r:
        if order.compare(right[0], left[0]): # left[0] <= right[0]
            array[k] = left[0]
            left.remove(left[0])
        else:
            array[k] = right[0]
            right.remove(right[0])
        k += 1
        swap_c += 1
        comp_c += 1
    while len(left) != 0:
        array[k] = left[0]
        left.remove(left[0])
        k += 1
        swap_c += 1
    while len(right) != 0:
        array[k] = right[0]
        right.remove(right[0])
        k += 1
        swap_c += 1
    __update_stats(lambda s: (s.inc_comp(comp_c), s.inc_swap(swap_c)), stats)

# Quicksort

def quicksort(array, p=0, q=None, order=Order.LE, stats=None):
    if q is None:
        q = len(array) - 1
    if p < q:
        x = partition(array, p, q, order, stats)
        quicksort(array, p, x-1, order, stats)
        quicksort(array, x+1, q, order, stats)

def partition(array, p, q, order, stats):
    x = int(p + (q - p) / 2)
    val = array[x]
    swap(array, x, q)
    position = p

    comp_c = 0
    swap_c = 2
    for j in range(p, q):
        comp_c += 1
        if order.compare(val, array[j]): # array[j] < val
            swap(array, position, j)
            position += 1
            swap_c += 1
    swap(array, position, q)
    __update_stats(lambda s: (s.inc_comp(comp_c), s.inc_swap(swap_c)), stats)
    return position

# Dual Pivot Quicksort

def dual_pivot_quicksort(array, left=0, right=None, order=Order.LE, stats=None):
    if right is None:
        right = len(array) - 1
    if right > left:
        i, k = dual_pivot_partition(array, left, right, order, stats)
        dual_pivot_quicksort(array, left, i - 2, order, stats)
        dual_pivot_quicksort(array, i, k, order, stats)
        dual_pivot_quicksort(array, k + 2, right, order, stats)

def dual_pivot_partition(array, left, right, order, stats):
    if order.compare(array[left], array[right], eq=False): # array[right] < array[left]
        swap(array, left, right)
    p = array[left]
    q = array[right]

    i = j = left + 1
    k = right - 1
    d = 0
    swap_c = 0
    comp_c = 1
    while j <= k:
        if d >= 0:
            if order.compare(p, array[j], eq=False): # array[j] < p
                swap(array, i, j)
                i += 1
                j += 1
                d += 1
                swap_c += 1
            else:
                if order.compare(q, array[j], eq=False): # array[j] < q
                    j += 1
                else:
                    swap(array, j, k)
                    k -= 1
                    d -= 1
                    swap_c += 1
                comp_c += 1
        else:
            if order.compare(array[k], q, eq=False): #array[k] > q
                k -= 1
                d -= 1
            else:
                if order.compare(p, array[k], eq=False): #array[k] < p
                    rotate3(array, k, j, i)
                    i += 1
                    d += 1
                    swap_c += 3
                else:
                    swap(array, j, k)
                    swap_c += 1
                j += 1
                comp_c += 1
        comp_c += 1
    swap(array, left, i - 1)
    swap(array, right, k + 1)

    swap_c += 2
    __update_stats(lambda s: (s.inc_swap(swap_c), s.inc_comp(comp_c)), stats)
    return (i, k)

# Utilities

def __update_stats(callable, stats):
    if not stats is None:
        callable(stats)

def rotate3(array, i1, i2, i3):
    temp = array[i1]
    array[i1] = array[i2]
    array[i2] = array[i3]
    array[i3] = temp

def swap(array, i1, i2):
    temp = array[i1]
    array[i1] = array[i2]
    array[i2] = temp
