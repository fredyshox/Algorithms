//
//  djikstra.hpp
//  lista5
//
//  Created by Kacper Raczy on 02.06.2018.
//  Copyright © 2018 Kacper Raczy. All rights reserved.
//

#ifndef djikstra_hpp
#define djikstra_hpp

#include <iostream>
#include <vector>
#include <climits>
#include "priority_queue.hpp"

using ::std::vector;

/**
 * Finds shortest paths to all vertices from given source.
 * @param  graph  matrix representation of graph
 * @param  source source vertex index
 * @return distances to vertices from source
 */
vector<double> djikstra(vector<vector<double>> graph, int source);

#endif /* djikstra_hpp */
