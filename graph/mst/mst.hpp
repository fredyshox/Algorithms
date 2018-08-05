//
//  spanning_trees.hpp
//  lista5
//
//  Created by Kacper Raczy on 02.06.2018.
//  Copyright © 2018 Kacper Raczy. All rights reserved.
//

#ifndef spanning_trees_hpp
#define spanning_trees_hpp

#include <iostream>
#include <algorithm>
#include <tuple>
#include <vector>
#include <climits>
#include "priority_queue.hpp"
#include "graph.hpp"

using ::std::vector;
using ::std::tuple;

struct subset {
    int rank;
    int parent;
};

graph* mst_kruskal(graph* g);
graph* mst_prim(graph* g);
// subset operations
bool mst_comp(edge e1, edge e2);
void make_set(subset* set, int x);
int find_set(subset* sets, int x);
void union_set(subset* sets, int x, int y);

#endif /* spanning_trees_hpp */
