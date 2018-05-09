#!/usr/bin/env python
#
# main.py
#
# Copyright (c) 2018 Kacper Raczy. All rights reserved.

import argparse
from bsearch import binary_search

def print_err(*args, **kwargs):
    print(*args, **kwargs, file=sys.stderr)

def main():
    try:
        array_str = input('array: ')
        value = int(input('value: '))
        array = list(filter(lambda x: x != '', array_str.split(' ')))
        array = list(map(lambda s: int(s), array))
    except ValueError:
        print('Error: Invalid arguments.')

    if not all(array[i] <= array[i+1] for i in range(len(array) - 1)):
        print('Error: Array not sorted')
        return 2

    idx = binary_search(array, value)
    if idx >= 0:
        print('Found. Index: {}'.format(idx))
        return 1
    else:
        print('Not found.')
        return 0

if __name__ == '__main__':
    main()
