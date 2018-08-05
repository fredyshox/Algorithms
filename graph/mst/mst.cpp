//
//  spanning_trees.cpp
//  lista5
//
//  Created by Kacper Raczy on 02.06.2018.
//  Copyright © 2018 Kacper Raczy. All rights reserved.
//

#include "mst.hpp"

bool mst_comp(edge e1, edge e2) {
    return e1.weight < e2.weight;
}

// Subset operations

void make_set(subset* set, int x) {
    set->parent = x;
    set->rank = 0;
}

int find_set(subset* sets, int x) {
    if (sets[x].parent != x) {
        sets[x].parent = find_set(sets, sets[x].parent);
    }

    return sets[x].parent;
}

void union_set(subset* sets, int x, int y) {
    int xRoot = find_set(sets, x);
    int yRoot = find_set(sets, y);

    if (sets[xRoot].rank > sets[yRoot].rank) {
        sets[yRoot].parent = xRoot;
    } else {
        sets[xRoot].parent = yRoot;
        if (sets[xRoot].rank == sets[yRoot].rank) {
            sets[yRoot].rank += 1;
        }
    }
}

// MST Algorithms

graph* mst_kruskal(graph* g) {
    int size = g->v_count();
    vector<edge> edges = g->all_edges();
    graph* tree = new graph(size);
    subset* sets = new subset[size];
    for (int i = 0; i < size; i++) {
        make_set(&sets[i], i);
    }

    std::sort(edges.begin(), edges.end(), mst_comp);
    for(edge &e : edges) {
        if (find_set(sets, e.v1) != find_set(sets, e.v2)) {
            tree->add_edge(e.v1, e.v2, e.weight);
            union_set(sets, e.v1, e.v2);
        }
    }

    return tree;
}

graph* mst_prim(graph* g) {
    int size = g->v_count();
    double** gMatrix = g->adjacency_matrix();
    int root = 0; // first vertex as root (start)
    vector<double> key(size, LONG_MAX);
    vector<bool> inMst(size, false);
    vector<int> parent(size, -1);
    priority_queue<int> queue;

    for (int i = 0; i < size; i++) {
        queue.insert(i, LONG_MAX);
    }
    key[root] = 0;
    queue.dec_priority(root, 0);

    int current;
    double weight;
    while (!queue.is_empty()) {
        current = queue.pop();
        inMst[current] = true;
        for (int v = 0; v < size; v++) {
            weight = gMatrix[current][v];
            if (weight && !inMst[v] && weight < key[v]) {
                key[v] = weight;
                queue.dec_priority(v, * (long *) &weight); // floating point bit level hack
                parent[v] = current;
            }
        }
    }

    graph* parent_graph = new graph(size);
    for(int i = 0; i < size; i++) {
        if (parent[i] >= 0) {
            parent_graph->add_edge(i, parent[i], gMatrix[i][parent[i]]);
        }
    }

    return parent_graph;
}
