/**
* linked_list/llist.h
*
* Copyright (c) 2018 Kacper Rączy
*/

#include <stdio.h>
#include <stdlib.h>
#include <time.h>

typedef struct Node {
  int value;
  struct Node* next;
} Node;

typedef struct List {
  Node *head;
} List;

Node*  node_create(int value);
List* list_create();
Node* get_elem(List* list, int index);
void append(List* list, int value);
void remove_elem(List* list, int index);
List* copy(List* l);
void merge(List* l1, List* l2);
void print_list(List* list);
