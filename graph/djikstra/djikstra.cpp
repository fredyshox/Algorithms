//
//  djikstra.cpp
//  lista5
//
//  Created by Kacper Raczy on 02.06.2018.
//  Copyright © 2018 Kacper Raczy. All rights reserved.
//

#include "djikstra.hpp"

vector<double> djikstra(vector<vector<double>> graph, int source) {
    vector<double> dist(graph.size());
    priority_queue<int> queue;

    for(int i = 0; i < graph.size(); i++) {
        queue.insert(i, LONG_MAX);
        dist[i] = LONG_MAX;
    }
    queue.dec_priority(source, 0);
    dist[source] = 0;
    int current;
    double weight;
    while (!queue.is_empty()) {
        current = queue.pop();
        for (int v = 0; v < graph[current].size(); v++) {
            weight = graph[current][v];
            if (weight && dist[v] > dist[current] + weight) {
                dist[v] = dist[current] + weight;
                // ugly floating point bit level hack to convert double to long
                queue.dec_priority(v, * (long *) &dist[v]);
            }
        }
    }

    return dist;
}
