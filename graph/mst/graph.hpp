//
//  graph.hpp
//  lista5
//
//  Created by Kacper Raczy on 02.06.2018.
//  Copyright © 2018 Kacper Raczy. All rights reserved.
//

#ifndef graph_hpp
#define graph_hpp

#include <iostream>
#include <vector>
#include <cassert>

using ::std::vector;

struct edge {
    int v1;
    int v2;
    double weight;
};

class graph {
public:
    graph(int v);
    void add_edge(int v1, int v2, double weight);
    double** adjacency_matrix();
    vector<edge> all_edges();
    int v_count();
    double total_weight();
private:
    int vCount;
    vector<edge> edges;
};

#endif /* graph_hpp */
