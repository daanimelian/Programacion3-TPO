# AdoptMe Algorithm Testing Guide

## Overview

This document describes the comprehensive test suite for all algorithms implemented in AdoptMe.

## Test Coverage

### 1. Graph Traversal (BFS/DFS)
- **Simple paths**: Adjacent nodes (A→B)
- **Long paths**: Cross-network (A→O)
- **Cross-cluster**: Between different graph regions (E→M)
- **Hub routing**: Through central hub (H→N)

**Test Cases:**
- BFS: 4 test cases
- DFS: 4 test cases
- **Total: 8 tests**

### 2. Dijkstra Shortest Path
- **Adjacent nodes**: Direct connections (H→A)
- **Long paths**: Multiple hops (A→M)
- **Cross-cluster**: Different regions (E→O)
- **Full network**: Maximum distance (A→N)
- **Hub routing**: Optimal via hub (B→L)

**Test Cases: 5 tests**

### 3. TSP Branch & Bound
- **Small (4 nodes)**: A,B,C,H - Quick computation
- **Medium cluster 1 (5 nodes)**: A,D,E,I,H - Regional tour
- **Medium cluster 2 (5 nodes)**: B,F,J,K,H - Different region
- **Large (7 nodes)**: A,B,C,G,L,M,H - Multi-cluster
- **Complex (8 nodes)**: H,A,D,E,F,J,L,O - Near limit

**Test Cases: 5 tests**

### 4. Minimum Spanning Tree (MST)
- **Kruskal**: Full 15-node network with 39 edges
- **Prim**: Full 15-node network with 39 edges

**Test Cases: 2 tests**

### 5. Sorting Algorithms
Tests all 40 dogs with different criteria and algorithms:
- **Priority sorting**: MergeSort & QuickSort (range: 3-10)
- **Age sorting**: MergeSort & QuickSort (range: 1-8 years)
- **Weight sorting**: MergeSort & QuickSort (range: 6-35 kg)

**Test Cases: 6 tests**

### 6. Greedy Matching
Tests various adopter profiles:
- **High budget**: P9 Julia ($40K), P14 Andres ($45K)
- **Low budget**: P10 Carlos ($12K)
- **With kids**: P1 Camila
- **No yard**: P8 Diego
- **High capacity**: P11 Valeria (max: 5 dogs)

**Test Cases: 6 tests**

### 7. Backtracking Constraint Satisfaction
- **Multi-adopter**: 15 adopters, 30+ available dogs
- Tests all constraints: budget, capacity, kids compatibility, yard requirements

**Test Cases: 1 test**

### 8. Knapsack Dynamic Programming
Tests different vehicle capacities:
- **Small (30kg)**: ~3-6 dogs
- **Medium (60kg)**: ~6-10 dogs
- **Large (100kg)**: ~12-18 dogs
- **Very large (200kg)**: ~20-30 dogs
- **Extreme (500kg)**: Can fit all 40 dogs

**Test Cases: 5 tests**

### 9. Data Integrity
- Verify 15 shelters loaded
- Verify 40 dogs loaded
- Verify 15 adopters loaded

**Test Cases: 3 tests**

## Total Test Count: 45 tests

## Running the Tests

### Prerequisites
1. Start the Spring Boot backend:
   ```bash
   cd /home/user/Programacion3-TPO
   ./mvnw spring-boot:run
   ```

2. Wait for the application to start (usually 10-20 seconds)

### Execute Test Suite

**Option 1: Bash Script (Recommended)**
```bash
./test-algorithms.sh
```

**Option 2: Manual Testing**
Use curl or the web frontend at `http://localhost:8080/frontend/index.html`

### Test Output

The script provides:
- **Real-time console output**: Color-coded pass/fail indicators
- **Detailed log file**: `test-results.log` with full request/response data
- **Summary statistics**: Total, passed, failed, and pass rate

### Expected Results

All 45 tests should pass with the current dataset:
- ✅ 15 shelters (A-O)
- ✅ 40 dogs with varied characteristics
- ✅ 15 adopters with different profiles
- ✅ 39 bidirectional edges in the network

## Test Case Details

### Complex Test Scenarios

#### 1. Long-Distance Path (A→O)
- **Distance**: Traverses entire network
- **Expected**: Multiple hops through clusters
- **Tests**: BFS, DFS, Dijkstra optimality

#### 2. TSP with 8 Nodes
- **Complexity**: O(n!) = 40,320 permutations
- **Nodes**: H,A,D,E,F,J,L,O
- **Expected**: Branch & bound pruning significantly reduces search space
- **Time**: Should complete in <10 seconds

#### 3. Backtracking Multi-Assignment
- **Complexity**: 15 adopters × 30 dogs with constraints
- **Search space**: Massive with pruning
- **Constraints tested**:
  - Budget limits
  - Max dogs per adopter
  - Kids compatibility
  - Yard requirements for large dogs
  - Energy level preferences

#### 4. Sorting 40 Dogs
- **Input size**: 40 elements
- **Algorithms**: MergeSort O(n log n), QuickSort O(n log n) avg
- **Criteria**: Priority (3-10), Age (1-8), Weight (6-35)
- **Verification**: Results should be in ascending order

#### 5. Knapsack 500kg
- **Test**: Can all 40 dogs fit?
- **Total weight**: Sum of all dog weights
- **Expected**: Optimal subset maximizing priority

## Performance Benchmarks

Expected execution times (approximate):

| Algorithm | Test Case | Expected Time |
|-----------|-----------|---------------|
| BFS/DFS | Simple path | <100ms |
| BFS/DFS | Complex path | <200ms |
| Dijkstra | Any path | <150ms |
| TSP | 4 nodes | <100ms |
| TSP | 7 nodes | <1s |
| TSP | 8 nodes | <10s |
| MST | 15 nodes | <200ms |
| Sort | 40 items | <50ms |
| Greedy | Any adopter | <100ms |
| Backtracking | 15 adopters | <5s |
| Knapsack | Any capacity | <100ms |

## Interpreting Results

### Success Indicators
- ✅ HTTP 200 status
- ✅ Response contains expected fields (totalScore, totalWeight, route, etc.)
- ✅ No error messages in response
- ✅ Reasonable execution time

### Failure Indicators
- ❌ HTTP 4xx/5xx status
- ❌ Missing expected fields
- ❌ Empty results when data exists
- ❌ Timeout (>30 seconds)

### Common Issues

**Issue**: "Server is not running"
**Solution**: Start backend with `./mvnw spring-boot:run`

**Issue**: TSP timeout
**Solution**: Reduce number of nodes (recommended max: 8-9)

**Issue**: Empty results
**Solution**: Check database seeding completed successfully

## Advanced Testing

### Manual Validation Examples

**1. Verify Dijkstra optimality:**
```bash
# Should return path with minimum total distance
curl "http://localhost:8080/routes/shortest?from=A&to=M"
```

**2. Compare MST algorithms:**
```bash
# Both should return same total weight (but possibly different edge selection)
curl "http://localhost:8080/network/mst?algorithm=kruskal"
curl "http://localhost:8080/network/mst?algorithm=prim"
```

**3. Verify sorting correctness:**
```bash
# Output should be in ascending order of priority
curl "http://localhost:8080/dogs/sort?criteria=priority&algorithm=mergesort"
```

## Contributing

To add new test cases:

1. Add test case to `test-algorithms.sh`
2. Use `run_test` function with descriptive name
3. Update this documentation
4. Ensure test is deterministic (same input → same output)

## Test Data Summary

### Shelters (15)
- IDs: A, B, C, D, E, F, G, H, I, J, K, L, M, N, O
- Capacities: 10-40 dogs
- Network: 39 bidirectional edges

### Dogs (40)
- IDs: D1-D40
- Sizes: SMALL (13), MEDIUM (14), LARGE (13)
- Ages: 1-8 years
- Weights: 6-35 kg
- Priorities: 3-10
- Special needs: 8 dogs
- Good with kids: 28 dogs

### Adopters (15)
- IDs: P1-P15
- Budgets: $12,000-$45,000
- With yard: 11 adopters
- With kids: 8 adopters
- Max capacity: 1-5 dogs

### Network Topology
- 4 regional clusters + 1 central hub
- Cluster 1: A-D-E-I (4 nodes)
- Cluster 2: B-F-J-K (4 nodes)
- Cluster 3: C-G-M-N (4 nodes)
- Cluster 4: L-O (2 nodes)
- Hub: H (connects all clusters)
- Shortest edge: 5km (B-F)
- Longest edge: 22km (D-L)

## Continuous Integration

Recommended CI pipeline:
1. Start Spring Boot application
2. Wait for healthy status
3. Run `./test-algorithms.sh`
4. Fail build if any test fails
5. Archive `test-results.log`

## License

This test suite is part of the AdoptMe project - UADE Programación III 2024.
