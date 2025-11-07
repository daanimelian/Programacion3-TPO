# AdoptMe Test Suite

This directory contains comprehensive unit tests for all algorithms implemented in AdoptMe.

## Files

- **`../test-algorithms.sh`** - Bash test script (45+ tests)
- **`../test_algorithms_advanced.py`** - Python advanced test suite with deep validation
- **`../TESTING.md`** - Complete testing documentation
- **`../TEST_REPORT_TEMPLATE.md`** - Template for manual test reports
- **`../QUICK_TEST_GUIDE.md`** - Quick reference guide

## Quick Start

```bash
# 1. Start the backend
./mvnw spring-boot:run

# 2. Run tests (choose one)
./test-algorithms.sh                    # Bash (simple)
python3 test_algorithms_advanced.py      # Python (advanced)
```

## Test Coverage

✅ **45+ Total Tests** covering all algorithms:

- 8 tests: BFS/DFS Graph Traversal
- 5 tests: Dijkstra Shortest Path
- 5 tests: TSP Branch & Bound
- 3 tests: MST (Kruskal & Prim)
- 6 tests: Sorting (MergeSort & QuickSort)
- 6 tests: Greedy Matching
- 1 test: Backtracking Constraint Satisfaction
- 5 tests: Knapsack Dynamic Programming
- 3 tests: Data Integrity
- 3+ tests: Cross-validation

## Test Data

- **15 Shelters** (A-O) with 39 network connections
- **40 Dogs** with varied characteristics
- **15 Adopters** with different profiles

See `../TESTING.md` for detailed test data specifications.

## Success Criteria

All tests should pass with:
- ✅ HTTP 200 status
- ✅ Valid response structure
- ✅ Correct algorithm output
- ✅ Reasonable performance

## Documentation

- **Quick Start:** `../QUICK_TEST_GUIDE.md`
- **Full Documentation:** `../TESTING.md`
- **Test Report Template:** `../TEST_REPORT_TEMPLATE.md`
