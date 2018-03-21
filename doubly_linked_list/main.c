/**
* doubly_linked_list/main.c
*
* Copyright (c) 2018 Kacper Rączy
*/

#include <stdio.h>
#include <time.h>
#include "dllist.h"

// MARK: TESTS

double avg(double tab[], int n) {
  double sum = 0;
  for (int i = 0; i<n; i++) {
    sum += tab[i];
  }
  return sum / (double) n;
}

void test() {
  List list;
  clock_t t;

  list = *list_create();
  srand(1);
  for(int i = 0; i<1000; i++) {
    append(&list, rand());
  }

  int cases[] = {200, 500, 700, 999};
  double times[1000];

  for(int i = 0; i<4; i++) {
    for(int j = 0; j<1000; j++) {
      t = clock();
      Node* n = get_elem(&list, cases[i]);
      t = clock() - t;
      times[j] = (double) t / CLOCKS_PER_SEC;
    }
    printf("Average access time to element %d: %f\n", cases[i], avg(times, 1000));
  }
}

void test_random() {
  List list;
  clock_t t;

  list = *list_create();
  srand(1);
  for(int i = 0; i<1000; i++) {
    append(&list, rand());
  }

  double times[1000];

  for(int j = 0; j<1000; j++) {
    t = clock();
    Node* n = get_elem(&list, rand() % 1000);
    t = clock() - t;
    times[j] = (double) t / CLOCKS_PER_SEC;
  }
  printf("Average access time to random element: %f\n", avg(times, 1000));
}

// MARK: MAIN

int main() {
  test();
  printf("-----RANDOM----\n");
  test_random();

  return 0;
}
