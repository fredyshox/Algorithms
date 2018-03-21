/**
* doubly_linked_list/dllist.c
*
* Copyright (c) 2018 Kacper Rączy
*/

#include "dllist.h"

Node* node_create(int value) {
  Node* n = malloc(sizeof(Node));
  n->value = value;
  n->next = n;
  n->prev = n;

  return n;
}

List* list_create() {
  List* l = malloc(sizeof(List));
  l->head = NULL;
  l->size = 0;

  return l;
}

Node* get_elem(List* l, int index) {
  if (index > (l->size - 1)) {
    printf("Index out of bounds.\n");
    return NULL;
  }

  Node* current = l->head;
  int niter;

  if (index > (l->size/2)) {
    niter = l->size - index;
    for(int i = 0; i<niter; i++) {
      current = current->prev;
    }
  } else {
    niter = index;
    for(int i = 0; i<niter; i++) {
      current = current->next;
    }
  }

  return current;
}

void append(List* list, int value) {
  Node* head = list->head;
  Node* new = node_create(value);
  list->size += 1;
  if (head == NULL) {
    list->head = new;
    return;
  }

  Node* prev = head->prev;
  prev->next = new;
  new->prev = prev;
  new->next = head;
  head->prev = new;
}

List* copy(List* l) {
  List* cpy = list_create();
  Node* current = l->head;

  if (current != NULL) {
    for(int i = 0; i < l->size; i++) {
      append(cpy, current->value);
      current = current->next;
    }
  }

  return cpy;
}

void merge(List* l1, List* l2) {
  Node* l1_head = l1->head;
  Node* l2_head = copy(l2)->head; //copy of l2

  if(l1_head == NULL) {
      l1->head = l2->head;
      return;
  }

  Node* l1_tail = l1_head->prev;
  Node* l2_tail = l2_head->prev;

  l1_tail->next = l2_head;
  l1_head->prev = l2_tail;
  l2_head->prev = l1_tail;
  l2_tail->next = l1_head;

  l1->size += l2->size;
}
