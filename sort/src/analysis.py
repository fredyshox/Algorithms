#!/usr/bin/env python
#
# analysis.py
#
# Copyright (c) 2018 Kacper Raczy. All rights reserved.

import argparse
import time
import sys
import random

from sort import *

import pandas as pd
import matplotlib.pyplot as plt

# Utility

def print_err(*args, **kwargs):
    print(*args, **kwargs, file=sys.stderr)

def parse_int(n):
    try:
        return int(n)
    except ValueError:
        return None

# Analysis

def analyze(sort_func, array, order=Order.LE):
    """ Sorting method wrapper.

    Execute sorting method on specified array.
    Return Stats object filled with statistics.
    """
    stats = Stats(len(array))
    start = time.time()
    sort_func(array, order=order, stats=stats)
    end = time.time()

    stats.time = end - start
    return stats

def analyze_random(count, output=None, input=None):
    """ Perform analysis using random arrays of sizes in 100...10000,
        and plot them.

    input -- input csv file
    output -- output file name
    """
    print_err('Random analysis started...')

    if input is None:
        row_list = []
        alg_list = [(merge_sort, Algorithm.MERGE),
                    (quicksort, Algorithm.QUICK),
                    (dual_pivot_quicksort, Algorithm.DPQUICK),
                    (radix_sort, Algorithm.RADIX),
                    (hybrid_sort, Algorithm.HYBRID)]
        for n in range(100, 10100, 100):
            for func, alg in alg_list:
                for _ in range(count):
                    arr = random.sample(range(n), n)
                    d = vars(analyze(func, arr))
                    d['algorithm'] = alg.name.lower()
                    row_list.append(d)
                    del arr
            print_err("COMPLETED {} OK".format(n))
        df = pd.DataFrame(row_list)
        if not output is None:
            df.to_csv(output)
            print("File saved")
    else:
        df = pd.read_csv(input)

    df['ncomp/n'] = np.where(df['length'] < 1, df['length'], df['ncomp']/df['length'])
    df['nswap/n'] = np.where(df['length'] < 1, df['length'], df['nswap']/df['length'])
    grouped = df.groupby(['length', 'algorithm']).mean(numeric_only=True)

    ncomp_g = grouped.loc[:, ['ncomp']]
    ncomp_g = pd.pivot_table(ncomp_g, values='ncomp', index='length', columns='algorithm')
    nswap_g = grouped.loc[:, ['nswap']]
    nswap_g = pd.pivot_table(nswap_g, values='nswap', index='length', columns='algorithm')
    time_g = grouped.loc[:, ['time']]
    time_g = pd.pivot_table(time_g, values='time', index='length', columns='algorithm')
    ncomp_n_g = grouped.loc[:, ['ncomp/n']]
    ncomp_n_g = pd.pivot_table(ncomp_n_g, values='ncomp/n', index='length', columns='algorithm')
    nswap_n_g = grouped.loc[:, ['nswap/n']]
    nswap_n_g = pd.pivot_table(nswap_n_g, values='nswap/n', index='length', columns='algorithm')

    # show plots
    ncomp_g.plot(title='Number of comparsions')
    nswap_g.plot(title='Number of swaps')
    time_g.plot(title='Time')
    ncomp_n_g.plot(title='Number of comparsions / n')
    nswap_n_g.plot(title='Number of swaps / n')
    plt.show()

# Main

def main():
    parser = argparse.ArgumentParser(description='Sorting algorithm analyser.')
    group = parser.add_argument_group('Required argument options (one of them is required)')
    group.add_argument('--output',
                       help='analysis output [filename] [k]',
                       metavar='OUT',
                       type=str,
                       nargs=2,
                       dest='statistics')
    group.add_argument('--input',
                       help='input file',
                       type=str, nargs=1,
                       dest='input')
    group.add_argument
    args = parser.parse_args()

    if not args.input is None:
        analyze_random(10, input=args.input[0])
        return 0

    if args.statistics is None:
        parser.print_usage()
        return 1

    filename = args.statistics[0]
    k = parse_int(args.statistics[1])
    if isinstance(k, int) and k > 0:
        analyze_random(k, output=filename)
        return 0
    else:
        parser.error('Invalid --output argument')
        return 2


if __name__ == '__main__':
    main()
