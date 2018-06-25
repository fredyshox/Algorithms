//
//  main.cpp
//  lista5
//
//  Created by Kacper Raczy on 02.06.2018.
//  Copyright © 2018 Kacper Raczy. All rights reserved.
//

#include <iostream>
#include <vector>
#include <cassert>
#include <chrono>
#include "djikstra.hpp"

using namespace std::chrono;

vector<vector<double>> gen_graph() {
    int vCount;
    int eCount;
    std::cin >> vCount;
    std::cin >> eCount;

    vector<vector<double>> result(vCount, vector<double>(vCount, 0));
    int v1, v2;
    double weight;
    for (int i = 0; i < eCount; i++) {
        std::cin >> v1 >> v2 >> weight;
        assert(weight >= 0);
        // v index cast
        result[v1 - 1][v2 - 1] = weight;
        result[v2 - 1][v1 - 1] = weight;
    }

    return result;
}

void print_paths(vector<double> paths) {
    for (int i = 0; i < paths.size(); i++) {
        std::cout << i + 1 << " " << paths[i] << std::endl;
    }
}

int main() {
    int src;
    vector<vector<double>> graph = gen_graph();
    std::cin >> src;
    high_resolution_clock::time_point t1 = high_resolution_clock::now();
    vector<double> result = djikstra(graph, src - 1);
    high_resolution_clock::time_point t2 = high_resolution_clock::now();
    print_paths(result);
    std::cerr << "Duration " << duration_cast<microseconds>(t2 - t1).count() << std::endl;
}
