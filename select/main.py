#!/usr/bin/env python
#
# main.py
#
# Copyright (c) 2018 Kacper Raczy. All rights reserved.

import argparse
from enum import Enum
import random
import time
import sys
from select import *

class Mode(Enum):
    RANDOM = 0
    PERMUTATION = 1

def analyze(select_func, array, nelem):
    stats = Stats(len(array), nelem)
    start = time.time()
    val = select_func(array, nelem, stats=stats)
    end = time.time()
    stats.time = end - start
    print_selected(array, val)

    return stats


def print_selected(arr, selected):
    result = ''
    idx = arr.index(selected)
    for i,x in enumerate(arr):
        if i == idx:
            result += '[{0}]'.format(x)
        else:
            result += '{0}'.format(x)
        result += ' '
    print(result)

def print_err(*args, **kwargs):
    print(*args, **kwargs, file=sys.stderr)

def main():
    parser = argparse.ArgumentParser(description='Select program.')
    modes = parser.add_argument_group('Program modes')
    modes.add_argument('-r', help='randomize mode', dest='random_mode', action='store_true')
    modes.add_argument('-p', help='permutation mode', dest='perm_mode', action='store_true')
    args = parser.parse_args()

    if not (args.random_mode or args.perm_mode):
        parser.print_usage()
        return 1

    mode = Mode.RANDOM if args.random_mode else Mode.PERMUTATION

    try:
        length = int(input('length '))
        idx = int(input('index '))
        if idx < 1 or idx > length:
            raise ValueError()
    except ValueError:
        parser.error('Invalid input')
        return 2

    if mode == Mode.RANDOM:
        elements = random.sample(range(100*length), length)
    else:
        elements = list(range(1, length + 1))
        random.shuffle(elements)

    print_err('array: ' + str(elements))
    print_err('--- Running SELECT ---')
    stats = analyze(select, elements.copy(), idx - 1)
    print_err(stats)
    print_err('--- Running RANDOMIZED SELECT ---')
    rand_stats = analyze(randomized_select, elements.copy(), idx)
    print_err(rand_stats)

if __name__ == "__main__":
    main()
