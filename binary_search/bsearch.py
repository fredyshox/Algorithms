#!/usr/bin/env python
#
# bsearch.py
#
# Copyright (c) 2018 Kacper Raczy. All rights reserved.

from math import floor

# Statistics

class Stats(object):
    def __init__(self, length=0, kidx=0, ncomp=0, time=0.0):
        self.length = length
        self.kidx = kidx
        self.ncomp = ncomp
        self.time = time

    def inc_comp(self, n=1):
        self.ncomp += n

    def __repr__(self):
        return 'Stats(ncomp: {}, nswap: {}, time: {})'.format(self.ncomp, self.time)

# Binary Search

def binary_search(array, val, left=0, right=None, stats=None):
    if right is None:
        right = len(array) - 1

    if left > right:
        return -1
    else:
        mid = floor((left + right) / 2)
        __update_stats(lambda s: s.inc_comp(), stats)

        if array[mid] < val:
            return binary_search(array, val, left=mid+1, right=right, stats=stats)
        elif array[mid] > val:
            __update_stats(lambda s: s.inc_comp(), stats)
            return binary_search(array, val, left=left, right=mid-1, stats=stats)
        else:
            return mid

# Utility

def __update_stats(cb, stats):
    if not stats is None:
        cb(stats)
