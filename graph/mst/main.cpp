//
//  main.cpp
//  ex3-test
//
//  Created by Kacper Raczy on 02.06.2018.
//  Copyright © 2018 Kacper Raczy. All rights reserved.
//

#include <iostream>
#include <string.h>
#include "mst.hpp"

using namespace std;

graph* gen_graph() {
    int vCount;
    int eCount;
    cin >> vCount;
    cin >> eCount;
    
    graph* result = new graph(vCount);
    int v1, v2;
    double weight;
    for (int i = 0; i < eCount; i++) {
        cin >> v1 >> v2 >> weight;
        assert(weight >= 0);
        result->add_edge(v1 - 1, v2 - 1, weight);
    }
    
    return result;
}

void print_mst(graph* tree) {
    cout << "MST edges:" << endl;
    for (edge &e : tree->all_edges()) {
        cout << e.v1 + 1 << " " << e.v2 + 1 << " " << e.weight << endl;
    }
    cout << "Tree weight: " << tree->total_weight() << endl;
}

int main(int argc, const char * argv[]) {
    if (argc < 2) {
        cerr << "Provide program parameter: -p (prim) | -k (kruskal)" << endl;
        return 1;
    }
    
    graph* tree;
    if (strcmp(argv[1], "-p") == 0) {
        graph* g = gen_graph();
        tree = mst_prim(g);
    } else if (strcmp(argv[1], "-k") == 0) {
        graph* g = gen_graph();
        tree = mst_kruskal(g);
    } else {
        cerr << "Invalid parameter. Valid options: -p (prim) | -k (kruskal)" << endl;
        return 2;
    }
    
    print_mst(tree);

    return 0;
}
