# AdoptMe Algorithm Test Report

**Date:** _________________
**Tester:** _________________
**Environment:** Spring Boot 3.5.6 + Neo4j + 15 Shelters + 40 Dogs + 15 Adopters
**Test Suite Version:** 1.0

---

## Executive Summary

| Metric | Value |
|--------|-------|
| Total Tests | 45+ |
| Tests Passed | ____ |
| Tests Failed | ____ |
| Pass Rate | ___% |
| Execution Time | ____ seconds |

---

## Test Results by Category

### 1. Graph Traversal Algorithms (BFS/DFS) - 8 Tests

| # | Test Case | Input | Expected Output | Status | Actual Result |
|---|-----------|-------|-----------------|--------|---------------|
| 1 | BFS Simple Path | from=A, to=B | exists=true, path=[A,B] or [A,H,B] | ☐ Pass ☐ Fail | _____________ |
| 2 | BFS Long Path | from=A, to=O | exists=true, path length ≥ 3 | ☐ Pass ☐ Fail | _____________ |
| 3 | BFS Cross-Cluster | from=E, to=M | exists=true, valid path | ☐ Pass ☐ Fail | _____________ |
| 4 | BFS Hub Routing | from=H, to=N | exists=true, valid path | ☐ Pass ☐ Fail | _____________ |
| 5 | DFS Simple Path | from=A, to=B | exists=true, valid path | ☐ Pass ☐ Fail | _____________ |
| 6 | DFS Long Path | from=A, to=O | exists=true, valid path | ☐ Pass ☐ Fail | _____________ |
| 7 | DFS Cross-Cluster | from=E, to=M | exists=true, valid path | ☐ Pass ☐ Fail | _____________ |
| 8 | DFS Multiple Paths | from=I, to=K | exists=true, valid path | ☐ Pass ☐ Fail | _____________ |

**Notes:** _________________________________________________________________

---

### 2. Dijkstra Shortest Path - 5 Tests

| # | Test Case | Input | Expected Output | Status | Actual Result |
|---|-----------|-------|-----------------|--------|---------------|
| 9 | Adjacent Nodes | from=H, to=A | path=[H,A], distance=5km | ☐ Pass ☐ Fail | _____________ |
| 10 | Long Path | from=A, to=M | valid path, distance > 0 | ☐ Pass ☐ Fail | _____________ |
| 11 | Cross-Cluster | from=E, to=O | valid path, distance ≥ 18km | ☐ Pass ☐ Fail | _____________ |
| 12 | Full Network | from=A, to=N | valid path, distance > 0 | ☐ Pass ☐ Fail | _____________ |
| 13 | Hub Routing | from=B, to=L | optimal path via H | ☐ Pass ☐ Fail | _____________ |

**Notes:** _________________________________________________________________

---

### 3. TSP Branch & Bound - 5 Tests

| # | Test Case | Input Nodes | Expected Output | Status | Execution Time |
|---|-----------|-------------|-----------------|--------|----------------|
| 14 | Small (4 nodes) | A,B,C,H | route returns to start, distance > 0 | ☐ Pass ☐ Fail | _____ sec |
| 15 | Medium Cluster 1 | A,D,E,I,H (5) | valid tour, optimal distance | ☐ Pass ☐ Fail | _____ sec |
| 16 | Medium Cluster 2 | B,F,J,K,H (5) | valid tour, optimal distance | ☐ Pass ☐ Fail | _____ sec |
| 17 | Large Tour | A,B,C,G,L,M,H (7) | valid tour, optimal distance | ☐ Pass ☐ Fail | _____ sec |
| 18 | Complex Tour | H,A,D,E,F,J,L,O (8) | valid tour, distance > 0 | ☐ Pass ☐ Fail | _____ sec |

**Notes:** _________________________________________________________________

---

### 4. Minimum Spanning Tree (MST) - 3 Tests

| # | Test Case | Input | Expected Output | Status | Actual Result |
|---|-----------|-------|-----------------|--------|---------------|
| 19 | Kruskal Full Network | 15 nodes, 39 edges | 14 edges, total weight > 0 | ☐ Pass ☐ Fail | _____ km |
| 20 | Prim Full Network | 15 nodes, 39 edges | 14 edges, total weight > 0 | ☐ Pass ☐ Fail | _____ km |
| 21 | Kruskal == Prim Weight | both algorithms | same total weight | ☐ Pass ☐ Fail | _____________ |

**Notes:** _________________________________________________________________

---

### 5. Sorting Algorithms - 6 Tests

| # | Test Case | Criteria | Algorithm | Expected Output | Status | Verified Sorted? |
|---|-----------|----------|-----------|-----------------|--------|------------------|
| 22 | Sort by Priority | priority | MergeSort | 40 dogs, ascending order | ☐ Pass ☐ Fail | ☐ Yes ☐ No |
| 23 | Sort by Priority | priority | QuickSort | 40 dogs, ascending order | ☐ Pass ☐ Fail | ☐ Yes ☐ No |
| 24 | Sort by Age | age | MergeSort | 40 dogs, ascending order | ☐ Pass ☐ Fail | ☐ Yes ☐ No |
| 25 | Sort by Age | age | QuickSort | 40 dogs, ascending order | ☐ Pass ☐ Fail | ☐ Yes ☐ No |
| 26 | Sort by Weight | weight | MergeSort | 40 dogs, ascending order | ☐ Pass ☐ Fail | ☐ Yes ☐ No |
| 27 | Sort by Weight | weight | QuickSort | 40 dogs, ascending order | ☐ Pass ☐ Fail | ☐ Yes ☐ No |

**Notes:** _________________________________________________________________

---

### 6. Greedy Matching Algorithm - 6 Tests

| # | Test Case | Adopter | Budget | Constraints | Expected | Status | Actual |
|---|-----------|---------|--------|-------------|----------|--------|--------|
| 28 | High Budget | P9 Julia | $40,000 | Yard, Kids, Max:3 | 3 dogs assigned | ☐ Pass ☐ Fail | _____ dogs |
| 29 | Highest Budget | P14 Andres | $45,000 | Yard, No kids, Max:4 | 4 dogs assigned | ☐ Pass ☐ Fail | _____ dogs |
| 30 | Low Budget | P10 Carlos | $12,000 | No yard, No kids, Max:1 | 1 dog assigned | ☐ Pass ☐ Fail | _____ dogs |
| 31 | With Kids | P1 Camila | $25,000 | Yard, Kids, Max:2 | 2 kid-friendly dogs | ☐ Pass ☐ Fail | _____ dogs |
| 32 | No Yard | P8 Diego | $28,000 | No yard, No kids, Max:2 | 2 small/medium dogs | ☐ Pass ☐ Fail | _____ dogs |
| 33 | High Capacity | P11 Valeria | $32,000 | Yard, No kids, Max:5 | Up to 5 dogs | ☐ Pass ☐ Fail | _____ dogs |

**Notes:** _________________________________________________________________

---

### 7. Backtracking Constraint Satisfaction - 1 Test

| # | Test Case | Input | Expected Output | Status | Details |
|---|-----------|-------|-----------------|--------|---------|
| 34 | Multi-Adopter Assignment | 15 adopters, 30+ dogs | Multiple valid assignments, score > 0 | ☐ Pass ☐ Fail | _____ adopters matched |

**Constraints Verified:**
- ☐ Budget limits respected
- ☐ Max dogs per adopter not exceeded
- ☐ Kids compatibility enforced
- ☐ Yard requirements for large dogs
- ☐ No dog assigned to multiple adopters

**Notes:** _________________________________________________________________

---

### 8. Knapsack Dynamic Programming - 5 Tests

| # | Test Case | Capacity | Expected Output | Status | Actual Result |
|---|-----------|----------|-----------------|--------|---------------|
| 35 | Small Capacity | 30 kg | 3-6 dogs, max priority, weight ≤ 30kg | ☐ Pass ☐ Fail | _____ dogs, _____ priority |
| 36 | Medium Capacity | 60 kg | 6-10 dogs, max priority, weight ≤ 60kg | ☐ Pass ☐ Fail | _____ dogs, _____ priority |
| 37 | Large Capacity | 100 kg | 12-18 dogs, max priority, weight ≤ 100kg | ☐ Pass ☐ Fail | _____ dogs, _____ priority |
| 38 | Very Large | 200 kg | 20-30 dogs, max priority, weight ≤ 200kg | ☐ Pass ☐ Fail | _____ dogs, _____ priority |
| 39 | Extreme | 500 kg | All 40 dogs if optimal, weight ≤ 500kg | ☐ Pass ☐ Fail | _____ dogs, _____ priority |

**Optimization Verified:**
- ☐ Priority/weight ratio maximized
- ☐ Capacity not exceeded
- ☐ All selected dogs valid

**Notes:** _________________________________________________________________

---

### 9. Data Integrity - 3 Tests

| # | Test Case | Expected Count | Status | Actual Count |
|---|-----------|----------------|--------|--------------|
| 40 | Shelters Loaded | 15 shelters | ☐ Pass ☐ Fail | _____ |
| 41 | Dogs Loaded | 40 dogs | ☐ Pass ☐ Fail | _____ |
| 42 | Adopters Loaded | 15 adopters | ☐ Pass ☐ Fail | _____ |

**Data Quality Checks:**
- ☐ All shelters have IDs A-O
- ☐ All dogs have unique IDs D1-D40
- ☐ All adopters have unique IDs P1-P15
- ☐ Network has 39 bidirectional edges
- ☐ Dogs distributed across shelters
- ☐ Some adopters already have dogs

**Notes:** _________________________________________________________________

---

## Performance Metrics

| Algorithm | Average Time | Max Time | Min Time | Notes |
|-----------|--------------|----------|----------|-------|
| BFS/DFS | _____ ms | _____ ms | _____ ms | ________________ |
| Dijkstra | _____ ms | _____ ms | _____ ms | ________________ |
| TSP (4 nodes) | _____ ms | _____ ms | _____ ms | ________________ |
| TSP (7 nodes) | _____ sec | _____ sec | _____ sec | ________________ |
| TSP (8 nodes) | _____ sec | _____ sec | _____ sec | ________________ |
| MST Kruskal | _____ ms | _____ ms | _____ ms | ________________ |
| MST Prim | _____ ms | _____ ms | _____ ms | ________________ |
| Sorting | _____ ms | _____ ms | _____ ms | ________________ |
| Greedy | _____ ms | _____ ms | _____ ms | ________________ |
| Backtracking | _____ sec | _____ sec | _____ sec | ________________ |
| Knapsack | _____ ms | _____ ms | _____ ms | ________________ |

---

## Issues Found

### Critical Issues
1. _________________________________________________________________
2. _________________________________________________________________

### Non-Critical Issues
1. _________________________________________________________________
2. _________________________________________________________________

### Performance Issues
1. _________________________________________________________________
2. _________________________________________________________________

---

## Test Environment Details

| Component | Version/Config |
|-----------|---------------|
| Java | 21 |
| Spring Boot | 3.5.6 |
| Neo4j | bolt://localhost:7687 |
| Server Port | 8080 |
| OS | _________________ |
| CPU | _________________ |
| RAM | _________________ |

---

## Validation Criteria

### BFS/DFS
- ✓ Path exists when nodes are connected
- ✓ Path contains valid node sequence
- ✓ No repeated nodes (except potentially in DFS with backtracking)

### Dijkstra
- ✓ Returns shortest path by distance
- ✓ Total weight matches sum of edge weights in path
- ✓ Optimality verified against manual calculation

### TSP
- ✓ Route starts and ends at same node
- ✓ All requested nodes visited exactly once
- ✓ Total distance is sum of edge weights in route
- ✓ Branch & bound finds optimal or near-optimal solution

### MST
- ✓ n-1 edges for n nodes
- ✓ No cycles (tree property)
- ✓ All nodes connected
- ✓ Minimum total weight

### Sorting
- ✓ All 40 dogs present in result
- ✓ Sorted in ascending order by specified criteria
- ✓ No duplicate dogs
- ✓ Original dog data preserved

### Greedy
- ✓ Budget not exceeded
- ✓ Max dogs limit respected
- ✓ Kids compatibility enforced
- ✓ Yard requirements for large dogs
- ✓ Dogs sorted by score (descending)

### Backtracking
- ✓ All constraints satisfied
- ✓ No dog assigned to multiple adopters
- ✓ Global score maximized
- ✓ Valid assignment for each matched adopter

### Knapsack
- ✓ Capacity not exceeded
- ✓ Priority maximized for given capacity
- ✓ All selected dogs valid
- ✓ Optimal solution verified

---

## Recommendations

### Code Quality
_________________________________________________________________
_________________________________________________________________

### Performance
_________________________________________________________________
_________________________________________________________________

### Test Coverage
_________________________________________________________________
_________________________________________________________________

---

## Approval

**Tester Signature:** _________________  **Date:** _________

**Reviewer Signature:** _________________  **Date:** _________

---

## Appendix A: Sample Outputs

### Sample BFS Output (A→B)
```json
{
  "exists": true,
  "path": ["A", "B"],
  "steps": 1
}
```

### Sample Dijkstra Output (H→A)
```json
{
  "path": ["H", "A"],
  "totalWeight": 5.0,
  "steps": 1
}
```

### Sample TSP Output (A,B,C,H)
```json
{
  "route": ["A", "B", "C", "H", "A"],
  "totalDistanceKm": 28.0,
  "nodesExplored": 24
}
```

### Sample Greedy Output
```json
{
  "message": "Greedy algorithm executed successfully",
  "adopterId": "P9",
  "adopterName": "Julia",
  "assignedDogs": [
    {"dogId": "D13", "dogName": "Lucy", "cost": 7000.0},
    {"dogId": "D27", "dogName": "Zoe", "cost": 7000.0}
  ],
  "totalScore": 12.5,
  "totalCost": 14000.0
}
```

---

**End of Report**
