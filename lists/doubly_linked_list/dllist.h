/**
* doubly_linked_list/dllist.h
*
* Copyright (c) 2018 Kacper Rączy
*/

#include <stdio.h>
#include <stdlib.h>

typedef struct Node {
  int value;
  struct Node* next;
  struct Node* prev;
} Node;

typedef struct List {
  int size;
  struct Node* head;
} List;

Node*  node_create(int value);
List* list_create();
Node* get_elem(List* l, int index);
void append(List* list, int value);
List* copy(List* l);
void merge(List* l1, List* l2);
