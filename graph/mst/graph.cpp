//
//  graph.cpp
//  lista5
//
//  Created by Kacper Raczy on 02.06.2018.
//  Copyright © 2018 Kacper Raczy. All rights reserved.
//

#include "graph.hpp"

graph::graph(int v) {
    vCount = v;
    edges.clear();
}

void graph::add_edge(int v1, int v2, double weight) {
    assert(v1 < this->vCount);
    assert(v2 < this->vCount);
    edge e = { v1, v2, weight };
    
    this->edges.push_back(e);
}

vector<edge> graph::all_edges() {
    return vector<edge>(this->edges);
}

double** graph::adjacency_matrix() {
    double** matrix = new double*[vCount];
    for(int i = 0; i < vCount; i++) {
        matrix[i] = new double[vCount];
        memset(matrix[i], 0.0, sizeof(double) * vCount);
    }
    for (edge &e : edges) {
        matrix[e.v1][e.v2] = e.weight;
        matrix[e.v2][e.v1] = e.weight;
    }
    
    return matrix;
}

int graph::v_count() {
    return vCount;
}

double graph::total_weight() {
    double total = 0;
    for (edge &e : edges) {
        total += e.weight;
    }
    
    return total;
}

