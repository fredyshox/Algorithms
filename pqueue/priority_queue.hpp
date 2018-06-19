//
//  priority_queue.hpp
//  lista5
//
//  Created by Kacper Raczy on 31.05.2018.
//  Copyright © 2018 Kacper Raczy. All rights reserved.
//

#ifndef priority_queue_hpp
#define priority_queue_hpp

#include <iostream>
#include <string>
#include <sstream>
#include <vector>
#include <cmath>

template <typename T>
struct heap_node {
    T value;
    long priority;
};

/**
 * Min Heap Priority Queue implementation.
 */
template <typename T>
class priority_queue {
public:
    void insert(T value, long priority) {
        heap_node<T> node;
        node.value = value;
        node.priority = LONG_MAX;
        heap.push_back(node);
        this->dec_priority_idx((int) heap.size() - 1, priority);
    }

    bool is_empty() {
        return heap.empty();
    }

    T top() {
        // TODO null checks
        return heap.front().value;
    }

    T pop() {
        // TODO null checks
        T min = heap.front().value;
        heap.front() = heap.back();
        heap.pop_back();
        if (!heap.empty()) {
            min_heapify(0);
        }

        return min;
    }

    void dec_priority(T value, long priority) {
        for (int i = 0; i < heap.size(); i++) {
            if (heap[i].value != value) {
                continue;
            }
            this->dec_priority_idx(i, priority);
        }
    }

    bool contains(T value) {
        for (heap_node<T> &n : heap) {
            if (n.value == value) {
                return true;
            }
        }
        return false;
    }

    std::string to_string() {
        std::ostringstream oss;
        for (int i = 0; i < heap.size(); i++) {
            oss << "(" << heap[i].value << ", " << heap[i].priority << ") ";
        }

        return oss.str();
    }
private:
    std::vector<heap_node<T>> heap;

    void min_heapify(int index) {
        if (index >= heap.size()) {
            std::cerr << "Error: Index out of bounds" << std::endl;
            return;
        }

        heap_node<T> current = heap[index];
        int leftIdx = this->left(index);
        int rightIdx = this->right(index);
        int smallest;

        if (leftIdx < heap.size() && heap[leftIdx].priority < current.priority)
            smallest = leftIdx;
        else
            smallest = index;

        if (rightIdx < heap.size() && heap[rightIdx].priority < heap[smallest].priority) {
            smallest = rightIdx;
        }

        if (smallest != index) {
            swap(index, smallest);
            return this->min_heapify(smallest);
        }
    }

    void dec_priority_idx(int index, long newPriority) {
        if (newPriority > heap[index].priority) {
            std::cerr << "Error: new priority higher than existing" << std::endl;
            return;
        }

        heap[index].priority = newPriority;
        while (index > 0 && heap[parent(index)].priority > heap[index].priority) {
            swap(index, this->parent(index));
            index = this->parent(index);
        }
    }

    // MARK: Heap tree methods;
    int parent(int i) {
        return (int) floor((i - 1) / 2.0);
    };

    int left(int i) {
        return 2 * i + 1;
    }

    int right(int i) {
        return 2 * i + 2;
    }

    // MARK: Utility
    void swap(int i1, int i2) {
        heap_node<T> temp = heap[i2];
        heap[i2] = heap[i1];
        heap[i1] = temp;
    }
};

#endif /* priority_queue_hpp */
