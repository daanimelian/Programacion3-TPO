# Quick Test Guide

## Prerequisites

1. **Start Neo4j Database**
   ```bash
   # Make sure Neo4j is running at bolt://localhost:7687
   # Credentials: neo4j / neo4j123
   ```

2. **Start Spring Boot Backend**
   ```bash
   cd /home/user/Programacion3-TPO
   ./mvnw spring-boot:run
   ```

3. **Wait for Application Ready**
   - Look for: "Started AdoptmeApplication"
   - Usually takes 10-20 seconds

## Running Tests

### Option 1: Bash Script (Simple)
```bash
./test-algorithms.sh
```
- **Pros:** No dependencies, fast
- **Cons:** Basic validation only
- **Output:** Console + test-results.log

### Option 2: Python Script (Advanced)
```bash
python3 test_algorithms_advanced.py
```
- **Pros:** Deep validation, better error messages
- **Cons:** Requires Python 3 + requests library
- **Output:** Console with detailed results

### Option 3: Manual Testing via curl

**Test BFS:**
```bash
curl "http://localhost:8080/graph/reachable?from=A&to=O&method=bfs"
```

**Test Dijkstra:**
```bash
curl "http://localhost:8080/routes/shortest?from=A&to=M"
```

**Test TSP:**
```bash
curl "http://localhost:8080/routes/tsp/bnb?nodes=A,B,C,H"
```

**Test Greedy:**
```bash
curl "http://localhost:8080/adoptions/greedy?adopterId=P9"
```

### Option 4: Web Frontend
1. Open browser: `http://localhost:8080/frontend/index.html`
2. Click through tabs
3. Test each algorithm interactively

## Test Data Reference

### Shelters (15)
```
A, B, C, D, E, F, G, H (hub), I, J, K, L, M, N, O
```

### Dogs (40)
```
D1-D40
- Sizes: SMALL, MEDIUM, LARGE
- Weights: 6-35 kg
- Ages: 1-8 years
- Priorities: 3-10
```

### Adopters (15)
```
P1-P15
- Budgets: $12,000-$45,000
- High budget: P9 ($40K), P14 ($45K)
- Low budget: P10 ($12K), P5 ($15K)
- With kids: P1, P4, P7, P9, P12, P13, P15
- With yard: P1, P3, P4, P6, P7, P9, P11, P12, P14, P15
```

## Quick Validation

### 1. Check Data Loaded
```bash
curl http://localhost:8080/shelters | jq 'length'  # Should be 15
curl http://localhost:8080/dogs | jq 'length'      # Should be 40
curl http://localhost:8080/adopters | jq 'length'  # Should be 15
```

### 2. Test Each Algorithm Type

**Graph (BFS/DFS):**
```bash
curl "http://localhost:8080/graph/reachable?from=A&to=B&method=bfs" | jq '.exists'
# Should return: true
```

**Shortest Path (Dijkstra):**
```bash
curl "http://localhost:8080/routes/shortest?from=H&to=A" | jq '.totalWeight'
# Should return: 5.0
```

**TSP:**
```bash
curl "http://localhost:8080/routes/tsp/bnb?nodes=A,B,C,H" | jq '.totalDistanceKm'
# Should return: positive number
```

**MST:**
```bash
curl "http://localhost:8080/network/mst?algorithm=kruskal" | jq '.edges | length'
# Should return: 14 (n-1 for 15 nodes)
```

**Sorting:**
```bash
curl "http://localhost:8080/dogs/sort?criteria=priority&algorithm=mergesort" | jq '.dogs | length'
# Should return: 40
```

**Greedy:**
```bash
curl "http://localhost:8080/adoptions/greedy?adopterId=P9" | jq '.assignedDogs | length'
# Should return: 1-3 dogs
```

**Backtracking:**
```bash
curl "http://localhost:8080/adoptions/constraints/backtracking" | jq '.totalScore'
# Should return: positive number
```

**Knapsack:**
```bash
curl "http://localhost:8080/transport/optimal-dp?capacityKg=100" | jq '.selectedDogs | length'
# Should return: 12-18 dogs
```

## Expected Results Summary

| Algorithm | Input | Expected Output |
|-----------|-------|-----------------|
| BFS | A→O | Path exists, 4-6 hops |
| Dijkstra | A→M | Path ~25-35 km |
| TSP 4 nodes | A,B,C,H | Tour ~26-30 km |
| TSP 8 nodes | Complex | Tour, <10 sec |
| MST Kruskal | 15 nodes | 14 edges, ~60-90 km |
| MST Prim | 15 nodes | Same weight as Kruskal |
| Sort 40 dogs | Priority | Ascending 3→10 |
| Greedy P9 | $40K budget | 2-3 dogs |
| Greedy P10 | $12K budget | 1 dog max |
| Backtracking | 15 adopters | 5-10 matches |
| Knapsack 30kg | Small | 3-6 dogs |
| Knapsack 500kg | Huge | ~35-40 dogs |

## Troubleshooting

### "Server not running"
- Start backend: `./mvnw spring-boot:run`
- Wait for "Started AdoptmeApplication"

### "Connection refused"
- Check Neo4j is running
- Verify port 8080 not in use: `lsof -i :8080`

### "Empty results"
- Check database seeding: Look for "[SEED]" in console
- Restart app to re-seed: Ctrl+C then `./mvnw spring-boot:run`

### TSP timeout
- Reduce nodes to ≤8
- Use smaller clusters

### Tests fail with HTTP 500
- Check backend logs for errors
- Verify Neo4j credentials correct

## Test Timing Guidelines

| Test Suite | Expected Duration |
|------------|-------------------|
| Full bash script | 30-60 seconds |
| Full Python script | 30-60 seconds |
| Single algorithm | 1-5 seconds |
| TSP 8 nodes | 5-10 seconds |
| Backtracking | 2-5 seconds |

## Success Criteria

✅ **All tests should pass if:**
- Database has 15 shelters, 40 dogs, 15 adopters
- Network has 39 edges
- All algorithms implemented correctly
- No data corruption

🎯 **Target Pass Rate: 100%**

## Quick Commands Cheat Sheet

```bash
# Start backend
./mvnw spring-boot:run

# Run bash tests
./test-algorithms.sh

# Run Python tests
python3 test_algorithms_advanced.py

# Check data counts
curl -s http://localhost:8080/shelters | jq 'length'
curl -s http://localhost:8080/dogs | jq 'length'
curl -s http://localhost:8080/adopters | jq 'length'

# Test most complex algorithms
curl "http://localhost:8080/routes/tsp/bnb?nodes=H,A,D,E,F,J,L,O"
curl "http://localhost:8080/adoptions/constraints/backtracking"
curl "http://localhost:8080/transport/optimal-dp?capacityKg=500"
```

## Notes

- All tests are **deterministic** - same input = same output
- Tests can be run **multiple times** safely
- No database modifications during testing
- Safe to run in **parallel** (separate test runners)
- Results logged to `test-results.log`

---

**For detailed test documentation, see TESTING.md**
**For test report template, see TEST_REPORT_TEMPLATE.md**
