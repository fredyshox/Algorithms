/**
* linked_list/llist.c
*
* Copyright (c) 2018 Kacper Rączy
*/

#include "llist.h"

Node*  node_create(int value) {
  Node* new = malloc(sizeof(Node));
  new->value = value;
  new->next = NULL;

  return new;
}

List* list_create() {
  List* list = malloc(sizeof(List));
  list->head = NULL;

  return list;
}

Node* get_elem(List* list, int index) {
  Node* current = list->head;

  for(int i = 0; i<index && current != NULL; i++) {
    current = current->next;
  }

  return current;
}

void append(List* list, int value) {
  Node* current = list->head;

  Node* new = node_create(value);

  if (current == NULL) {
    list->head = new;
    return;
  }

  while (current->next != NULL) {
    current = current->next;
  }

  current->next = new;
}

void remove_elem(List* list, int index) {
  Node* current = list->head;
  Node* prev;

  int i;

  for (i = 0; i<index && current != NULL; i++) {
    current = current->next;
    if (i == index-2) {
      prev = current;
    }
  }

  if(i == index - 1) {
    prev->next = current->next;
    free(current);
  }else {
    perror("Index out of bounds\n");
  }
}

List* copy(List* l) {
  List* cpy = list_create();
  Node* cpy_current;
  Node* current = l->head;

  if (current != NULL) {
    cpy_current = node_create(current->value);
    cpy->head = cpy_current;

    while((current = current->next) != NULL) {
      cpy_current->next = node_create(current->value);
      cpy_current = cpy_current->next;
    }
  }

  return cpy;
}

void merge(List* l1, List* l2) {
  Node* current = l1->head;
  Node* l2cpy = copy(l2)->head;

  while (current->next != NULL) {
    current = current->next;
  }

  current->next = l2cpy;
}

void print_list(List* list) {
  Node* current = list->head;
  int counter = 0;

  while(current != NULL) {
    printf("Value %d: %d\n", counter, current->value);
    current = current->next;
    counter++;
  }
}
