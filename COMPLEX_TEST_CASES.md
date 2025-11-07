# Complex Algorithm Test Cases - Detailed Reference

This document lists all complex test cases designed to stress-test each algorithm with maximum data complexity.

## Overview

All tests use the expanded dataset:
- **15 Shelters** (A, B, C, D, E, F, G, H, I, J, K, L, M, N, O)
- **40 Dogs** (D1-D40) with varied characteristics
- **15 Adopters** (P1-P15) with different budgets and constraints
- **39 Bidirectional Edges** forming a dense network graph

---

## 1. Graph Traversal Algorithms

### BFS/DFS Complex Test Cases

| Test ID | Algorithm | From | To | Complexity | Expected Result | Validation |
|---------|-----------|------|----|-----------|-----------------| ------------|
| GT-001 | BFS | A | O | Maximum distance across network | Path exists, 4-6 hops | Path validity, no cycles |
| GT-002 | BFS | E | M | Cross-cluster (1→3) | Path exists via multiple routes | Shortest hop count |
| GT-003 | BFS | H | N | Hub to periphery | Path exists, 2-4 hops | Optimal path via hub |
| GT-004 | DFS | A | O | Deep search across network | Path exists, possibly longer | Valid path found |
| GT-005 | DFS | I | K | Multiple path options | Path exists via clusters | Any valid path |
| GT-006 | BFS | F | L | Inter-cluster routing | Path exists, 3-5 hops | Efficient routing |

**Complexity Factors:**
- Maximum node separation
- Multiple valid paths
- Cross-cluster navigation
- Hub-based routing
- Dense network topology

**Example Command:**
```bash
curl "http://localhost:8080/graph/reachable?from=A&to=O&method=bfs"
```

**Expected Output:**
```json
{
  "exists": true,
  "path": ["A", "D", "E", "O"],
  "steps": 3
}
```

---

## 2. Dijkstra Shortest Path

### Dijkstra Complex Test Cases

| Test ID | From | To | Path Length | Expected Distance | Complexity |
|---------|------|----|--------------|--------------------|------------|
| DJ-001 | A | M | 4-6 hops | 25-35 km | Long path, multiple routes |
| DJ-002 | E | O | 1-2 hops | 18 km (direct) | Direct edge exists |
| DJ-003 | A | N | 5-7 hops | 35-50 km | Maximum network distance |
| DJ-004 | B | L | 3-4 hops | 20-30 km | Hub routing optimization |
| DJ-005 | F | M | 3-5 hops | 22-32 km | Cross-cluster optimal path |

**Complexity Factors:**
- Weighted edges (5-22 km)
- Multiple path options
- Hub vs direct routing trade-offs
- Distance minimization
- Non-uniform edge weights

**Example Command:**
```bash
curl "http://localhost:8080/routes/shortest?from=A&to=M"
```

**Expected Output:**
```json
{
  "path": ["A", "D", "G", "M"],
  "totalWeight": 31.0,
  "steps": 3
}
```

**Validation:**
- Path weight = sum of edge weights
- Optimal among all possible paths
- All edges in path exist in graph

---

## 3. TSP Branch & Bound

### TSP Complex Test Cases

| Test ID | Nodes | Count | Estimated Distance | Permutations | Execution Time |
|---------|-------|-------|-------------------|--------------|----------------|
| TSP-001 | A,B,C,H | 4 | 26-30 km | 24 (4!/2) | <100 ms |
| TSP-002 | A,D,E,I,H | 5 | 35-45 km | 120 (5!/2) | <500 ms |
| TSP-003 | B,F,J,K,H | 5 | 30-40 km | 120 (5!/2) | <500 ms |
| TSP-004 | A,B,C,G,L,M,H | 7 | 55-75 km | 2,520 (7!/2) | 1-5 sec |
| TSP-005 | H,A,D,E,F,J,L,O | 8 | 70-100 km | 20,160 (8!/2) | 5-10 sec |
| TSP-006 | A,B,C,D,E,F,G,H,I | 9 | 80-120 km | 181,440 (9!/2) | 30-60 sec |

**Complexity Factors:**
- Factorial growth of search space
- Branch & bound pruning effectiveness
- Non-Euclidean distance matrix
- Asymmetric paths (though edges are symmetric)
- Multiple local optima

**Example Command:**
```bash
curl "http://localhost:8080/routes/tsp/bnb?nodes=H,A,D,E,F,J,L,O"
```

**Expected Output:**
```json
{
  "route": ["H", "A", "D", "E", "O", "L", "J", "F", "H"],
  "totalDistanceKm": 89.0,
  "nodesExplored": 15234
}
```

**Validation:**
- Route starts and ends at same node
- All nodes visited exactly once
- Distance = sum of consecutive edge weights
- Optimal or near-optimal solution

---

## 4. Minimum Spanning Tree

### MST Complex Test Cases

| Test ID | Algorithm | Nodes | Edges Input | Expected Edges | Expected Weight |
|---------|-----------|-------|-------------|----------------|-----------------|
| MST-001 | Kruskal | 15 | 39 | 14 | 60-90 km |
| MST-002 | Prim | 15 | 39 | 14 | 60-90 km |
| MST-003 | Cross-validation | 15 | 39 | Same weight | Match within 0.01 |

**Complexity Factors:**
- Full network (all 15 nodes)
- Dense graph (39 edges for 15 nodes)
- Union-Find optimization (Kruskal)
- Priority Queue efficiency (Prim)
- Non-uniform edge weights

**Example Command:**
```bash
curl "http://localhost:8080/network/mst?algorithm=kruskal"
```

**Expected Output:**
```json
{
  "edges": [
    {"from": "H", "to": "A", "weight": 5.0},
    {"from": "B", "to": "F", "weight": 5.0},
    {"from": "A", "to": "B", "weight": 6.0},
    ...
  ],
  "totalWeight": 78.0
}
```

**Validation:**
- Exactly n-1 edges for n nodes
- All nodes reachable (connected graph)
- No cycles
- Minimum total weight
- Kruskal ≈ Prim total weight

---

## 5. Sorting Algorithms

### Sorting Complex Test Cases

| Test ID | Algorithm | Criteria | Count | Range | Complexity |
|---------|-----------|----------|-------|-------|------------|
| SORT-001 | MergeSort | priority | 40 | 3-10 | O(n log n) stable |
| SORT-002 | QuickSort | priority | 40 | 3-10 | O(n log n) avg |
| SORT-003 | MergeSort | age | 40 | 1-8 | Integer comparison |
| SORT-004 | QuickSort | age | 40 | 1-8 | Partition strategy |
| SORT-005 | MergeSort | weight | 40 | 6-35 | Float comparison |
| SORT-006 | QuickSort | weight | 40 | 6-35 | Pivot selection |

**Complexity Factors:**
- Full dataset (40 dogs)
- Various data distributions
- Different data types (int, float)
- Stability requirements (MergeSort)
- Worst-case vs average-case performance

**Example Command:**
```bash
curl "http://localhost:8080/dogs/sort?criteria=priority&algorithm=mergesort"
```

**Expected Output:**
```json
{
  "dogs": [
    {"id": "D16", "name": "Duke", "priority": 3},
    {"id": "D5", "name": "Perchita", "priority": 3},
    {"id": "D1", "name": "Luna", "priority": 4},
    ...
  ],
  "criteria": "priority",
  "algorithm": "MergeSort"
}
```

**Validation:**
- All 40 dogs present
- Ascending order: dogs[i].criteria ≤ dogs[i+1].criteria
- No duplicates or missing dogs
- Stable sort preserves relative order

---

## 6. Greedy Matching

### Greedy Complex Test Cases

| Test ID | Adopter | Budget | Yard | Kids | Max Dogs | Expected Dogs | Complexity |
|---------|---------|--------|------|------|----------|---------------|------------|
| GREEDY-001 | P9 Julia | $40,000 | Yes | Yes | 3 | 3 | High compatibility |
| GREEDY-002 | P14 Andres | $45,000 | Yes | No | 4 | 4 | Maximum budget |
| GREEDY-003 | P10 Carlos | $12,000 | No | No | 1 | 1 | Minimum budget |
| GREEDY-004 | P1 Camila | $25,000 | Yes | Yes | 2 | 2 | Kids filter |
| GREEDY-005 | P8 Diego | $28,000 | No | No | 2 | 2 | No yard constraint |
| GREEDY-006 | P11 Valeria | $32,000 | Yes | No | 5 | 4-5 | High capacity |

**Scoring Formula:**
```
score = kids_match(3.0) + yard_match(2.0) + energy_pref(2.0) + size_pref(1.0)
Max score: 8.0 points
```

**Cost Formula:**
```
cost = 5000 (base) + size_cost + special_needs(5000)
- SMALL: $7,000
- MEDIUM: $9,000
- LARGE: $11,000
- +Special: +$5,000
```

**Complexity Factors:**
- Multi-criteria scoring
- Hard constraints (budget, kids, yard, capacity)
- Soft preferences (energy, size)
- Greedy selection order
- 40 dogs to evaluate

**Example Command:**
```bash
curl "http://localhost:8080/adoptions/greedy?adopterId=P9"
```

**Expected Output:**
```json
{
  "adopterId": "P9",
  "adopterName": "Julia",
  "assignedDogs": [
    {"dogId": "D13", "dogName": "Lucy", "cost": 7000.0},
    {"dogId": "D27", "dogName": "Zoe", "cost": 7000.0},
    {"dogId": "D9", "dogName": "Bella", "cost": 7000.0}
  ],
  "totalScore": 18.0,
  "totalCost": 21000.0
}
```

**Validation:**
- Budget: totalCost ≤ adopter.budget
- Capacity: dogs.length ≤ adopter.maxDogs
- Kids: if adopter.hasKids, all dogs.goodWithKids
- Yard: if dog.size=LARGE, adopter.hasYard
- Score: Dogs sorted by score descending

---

## 7. Backtracking Constraint Satisfaction

### Backtracking Complex Test Case

| Test ID | Adopters | Available Dogs | Constraints | Search Space | Complexity |
|---------|----------|----------------|-------------|--------------|------------|
| BT-001 | 15 | 30+ | All | Massive | NP-Complete |

**Constraints Enforced:**
1. **Budget**: Total cost ≤ adopter budget
2. **Capacity**: Dogs assigned ≤ maxDogs
3. **Kids**: If hasKids=true, only goodWithKids dogs
4. **Yard**: If large dog, need hasYard=true
5. **Energy**: Preference for moderate energy
6. **Uniqueness**: Each dog assigned to max 1 adopter

**Complexity Factors:**
- 15 adopters × 30 dogs = 450 possible assignments
- Multiple constraints per assignment
- Global optimization (maximize total score)
- Backtracking with pruning
- Constraint propagation

**Example Command:**
```bash
curl "http://localhost:8080/adoptions/constraints/backtracking"
```

**Expected Output:**
```json
{
  "assignments": {
    "P7": {
      "adopterId": "P7",
      "adopterName": "Ana",
      "assignedDogs": [{"dogId": "D17", "dogName": "Lola"}]
    },
    "P9": {
      "adopterId": "P9",
      "adopterName": "Julia",
      "assignedDogs": [
        {"dogId": "D13", "dogName": "Lucy"},
        {"dogId": "D27", "dogName": "Zoe"}
      ]
    },
    ...
  },
  "totalScore": 125.5
}
```

**Validation:**
- Each adopter respects all constraints
- No dog appears twice
- Global score is reasonable
- 5-10 adopters matched (not all may get dogs)
- Higher priority dogs assigned first

---

## 8. Knapsack Dynamic Programming

### Knapsack Complex Test Cases

| Test ID | Capacity (kg) | Expected Dogs | Expected Priority | Avg Weight/Dog | Complexity |
|---------|---------------|---------------|-------------------|----------------|------------|
| KS-001 | 30 | 3-6 | 40-55 | ~7-10 kg | Small constraint |
| KS-002 | 60 | 6-10 | 75-90 | ~8-12 kg | Medium constraint |
| KS-003 | 100 | 12-18 | 120-150 | ~10-15 kg | Moderate constraint |
| KS-004 | 200 | 20-30 | 180-220 | ~12-18 kg | Loose constraint |
| KS-005 | 500 | 35-40 | 250+ | ~15-20 kg | Near all dogs |

**Optimization Goal:**
```
Maximize: Σ(priority_i)
Subject to: Σ(weight_i) ≤ capacity
```

**Complexity Factors:**
- 40 items (dogs)
- Varying weights (6-35 kg)
- Varying values (priority 3-10)
- Non-integer weights (DP table size)
- Priority/weight ratios vary significantly

**Example Command:**
```bash
curl "http://localhost:8080/transport/optimal-dp?capacityKg=100"
```

**Expected Output:**
```json
{
  "vehicleCapacityKg": 100,
  "selectedDogs": [
    {"id": "D13", "name": "Lucy", "weightKg": 6, "priority": 10},
    {"id": "D27", "name": "Zoe", "weightKg": 6, "priority": 10},
    {"id": "D9", "name": "Bella", "weightKg": 7, "priority": 9},
    ...
  ],
  "totalWeightKg": 98.0,
  "totalPriority": 145
}
```

**Validation:**
- totalWeightKg ≤ capacityKg
- High priority dogs selected
- Efficient weight utilization
- DP table size manageable
- Optimal subset for given capacity

---

## Complexity Summary

### Algorithm Complexity Classes

| Algorithm | Time Complexity | Space Complexity | Test Case Complexity |
|-----------|----------------|------------------|----------------------|
| BFS | O(V + E) | O(V) | 15V + 39E |
| DFS | O(V + E) | O(V) | 15V + 39E |
| Dijkstra | O((V + E) log V) | O(V) | 15V + 39E, weighted |
| TSP B&B | O(n!) worst, pruned | O(n) | 8! = 40,320 states |
| Kruskal MST | O(E log E) | O(V) | 39E, Union-Find |
| Prim MST | O(E log V) | O(V) | 39E, Priority Queue |
| MergeSort | O(n log n) | O(n) | 40 items |
| QuickSort | O(n log n) avg | O(log n) | 40 items |
| Greedy | O(n log n) | O(n) | 40 dogs, score all |
| Backtracking | O(d^n) pruned | O(n) | 15×30 assignments |
| Knapsack DP | O(nW) | O(nW) | 40n × 500W |

### Data Complexity Metrics

| Metric | Value | Impact |
|--------|-------|--------|
| Network Density | 39/(15×14/2) = 37% | High connectivity |
| Max Path Length | ~6-7 hops | Deep searches |
| Edge Weight Range | 5-22 km (440%) | Non-uniform costs |
| Dog Variety | 13S, 14M, 13L | Balanced distribution |
| Budget Range | $12K-$45K (375%) | Wide constraint range |
| Priority Range | 3-10 (333%) | Significant optimization potential |

---

## Performance Benchmarks

### Expected Execution Times (Approximate)

| Algorithm | Simple Case | Complex Case | Extreme Case |
|-----------|-------------|--------------|--------------|
| BFS/DFS | <50 ms | <150 ms | <200 ms |
| Dijkstra | <50 ms | <150 ms | <200 ms |
| TSP 4 nodes | <50 ms | - | - |
| TSP 7 nodes | <500 ms | <3 sec | - |
| TSP 8 nodes | <2 sec | <10 sec | <30 sec |
| TSP 9 nodes | <10 sec | <60 sec | Timeout |
| MST | <100 ms | <200 ms | <300 ms |
| Sort | <30 ms | <50 ms | <100 ms |
| Greedy | <50 ms | <100 ms | <150 ms |
| Backtracking | <1 sec | <5 sec | <10 sec |
| Knapsack | <50 ms | <100 ms | <200 ms |

**Note:** Times measured on standard development machine (4-core CPU, 8GB RAM)

---

## Test Execution Priority

### Critical Path Tests (Must Pass)
1. Data Integrity (3 tests)
2. BFS/DFS basic (2 tests)
3. Dijkstra basic (2 tests)
4. MST basic (2 tests)
5. Sort basic (2 tests)

### High Priority Tests (Should Pass)
6. TSP small (1 test)
7. Greedy basic (2 tests)
8. Knapsack medium (1 test)

### Complex Tests (Performance Validation)
9. TSP large (2 tests)
10. Backtracking (1 test)
11. Cross-cluster routing (3 tests)

### Stress Tests (Optional)
12. TSP 9 nodes
13. Knapsack 500kg
14. All cross-validations

---

## Recommendations for Testing

1. **Start with simple tests** - Verify basic functionality
2. **Progress to complex** - Ensure algorithms handle full dataset
3. **Monitor performance** - Track execution times
4. **Validate correctness** - Not just HTTP 200, verify output
5. **Test edge cases** - Minimum/maximum inputs
6. **Cross-validate** - Compare algorithm variants (MST, Sort)
7. **Automate** - Use provided test scripts
8. **Document failures** - Use TEST_REPORT_TEMPLATE.md

---

**For automated execution of all complex tests, run:**
```bash
./test-algorithms.sh
python3 test_algorithms_advanced.py
```
