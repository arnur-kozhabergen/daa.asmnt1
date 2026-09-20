# Assignment 1: Divide-and-Conquer Algorithm Analysis

## Project overview

This project implements and measures four divide-and-conquer algorithms in Java:

- Merge Sort
- randomized Quick Sort
- Deterministic Select (Median of Medians)
- Closest Pair of Points

The program also checks correctness against simple reference methods and saves performance data in `results/results.csv`.

## How to run

The project uses Java 17 and Maven. Run the tests with:

```text
mvn test
```

Run the demonstration and experiments with:

```text
mvn compile
java -cp target/classes Main
```

`Main` prints a few examples and writes 64 experiment rows to `results/results.csv`.

## Algorithms and analysis

### Merge Sort

The array is divided into two halves, sorted recursively, and merged with one reusable auxiliary buffer. Subarrays of at most 16 elements use insertion sort.

- Recurrence: `T(n) = 2T(n/2) + Theta(n)`
- Time: `Theta(n log n)` in the best, average, and worst cases
- Extra space: `O(n)` for the buffer, plus `O(log n)` recursion stack

The recurrence is the standard Master Theorem case 2.

### Quick Sort

The pivot is selected randomly and partitioning is done in place. The method recursively processes the smaller partition and continues with the larger partition in a loop. This keeps the stack small even when the partitions are unbalanced.

- Average recurrence: approximately `T(n) = 2T(n/2) + Theta(n)`
- Average time: `O(n log n)`
- Worst recurrence: `T(n) = T(n-1) + Theta(n)`
- Worst time: `O(n^2)`
- Extra space: `O(log n)` stack for the smaller-first strategy, plus `O(1)` data space

### Deterministic Select

The array is split into groups of five. Every group is sorted, their medians are collected, and the median of those medians is used as the pivot. A three-way in-place partition handles duplicate values, and only the required partition is searched recursively.

- Recurrence: `T(n) = T(n/5) + T(7n/10) + Theta(n)`
- Time: `Theta(n)` worst case (Akra-Bazzi intuition)
- Extra space: `O(log n)` recursion stack and `O(1)` array space

### Closest Pair of Points

Points are sorted by `x` once. The recursive halves remain sorted by `y` after each merge. A central strip is checked for pairs that cross the division line.

- Recurrence: `T(n) = 2T(n/2) + Theta(n)`
- Time: `Theta(n log n)`
- Extra space: `O(n)` for the working buffer and `O(log n)` recursion stack

## Experiments

Each algorithm was measured for `n = 100, 500, 1000, 2000` on four input types:

- Random
- Sorted
- Reverse-sorted
- Duplicate-heavy

Each row is the average of three timed runs after one warm-up run. `System.nanoTime()` is used for timing. The program records maximum recursion depth and an additional operation metric: comparisons for the sorting and selection algorithms, and distance checks for Closest Pair.

Example results for random input (times depend on the JVM and computer):

| Algorithm | n | Average time (ns) | Max depth | Operations |
|---|---:|---:|---:|---:|
| MergeSort | 100 | 19,966 | 4 | 690 |
| QuickSort | 100 | 74,633 | 4 | 1,105 |
| Deterministic Select | 100 | 28,933 | 6 | 658 |
| Closest Pair | 100 | 113,500 | 7 | 166 |
| MergeSort | 1,000 | 59,600 | 7 | 10,278 |
| QuickSort | 1,000 | 80,966 | 7 | 13,339 |
| Deterministic Select | 1,000 | 38,800 | 9 | 7,573 |
| Closest Pair | 1,000 | 525,733 | 10 | 1,544 |
| MergeSort | 2,000 | 213,833 | 8 | 22,743 |
| QuickSort | 2,000 | 158,366 | 8 | 31,026 |
| Deterministic Select | 2,000 | 296,266 | 10 | 15,973 |
| Closest Pair | 2,000 | 1,430,233 | 11 | 3,098 |

Plots:

- [Time versus n](docs/plots/time_vs_n.png)
- [Recursion depth versus n](docs/plots/recursion_depth_vs_n.png)

The raw data is available in [results.csv](results/results.csv).

## Discussion

The results generally follow the theoretical curves: Merge Sort and Closest Pair grow close to `n log n`, while Deterministic Select grows more slowly. Quick Sort is also fast on these inputs, but its measured time changes because the pivot is random and the JVM is affected by warm-up and garbage collection.

Sorted and reverse-sorted inputs can change Quick Sort's partition behavior, while duplicate-heavy inputs make three-way partitioning useful. The smaller-first recursion rule is important because it limits the recursion stack to logarithmic size even when the larger partition is processed iteratively.

Median of Medians guarantees a sufficiently balanced pivot: groups of five ensure that a constant fraction of elements is removed on every level, which gives the linear recurrence above. The divide-and-conquer Closest Pair algorithm checks only a small number of points in the central strip, so it is much faster than the `O(n^2)` brute-force method for large datasets.

Practical measurements are affected by JVM JIT compilation, cache behavior, allocation, garbage collection, operating-system scheduling, and the fact that the experiment uses relatively small input sizes.

## Testing

Sorting tests compare Merge Sort and Quick Sort with `Arrays.sort()` on empty, single-element, sorted, reverse-sorted, duplicate-heavy, random, negative, and extreme integer values. Deterministic Select has 200 random tests and compares with `Arrays.sort(array)[k]`. Closest Pair has 100 random tests against a brute-force `O(n^2)` method.

The readable test output is saved in [test-results.png](docs/screenshots/test-results.png). A sample program run is shown in [program-output.png](docs/screenshots/program-output.png).

## Reflection

The main lesson was that a correct divide-and-conquer solution also needs a careful complexity analysis. The most challenging parts were keeping one buffer for Merge Sort, making Quick Sort recurse only into the smaller partition, and maintaining `y` order in Closest Pair without sorting the strip at every recursion level.

The experiments also showed that theoretical complexity is a guide rather than an exact timing prediction. JVM warm-up, input shape, duplicate values, and memory behavior can noticeably change the measured result, so the same algorithm can have different timings on different runs.

## Repository structure

```text
src/
  MergeSorter.java
  QuickSorter.java
  DeterministicSelector.java
  ClosestPairSolver.java
  Point.java
  Experiment.java
  Main.java
tests/
docs/
  screenshots/
  plots/
results/
  results.csv
README.md
pom.xml
.gitignore
```

The Git history was built in small student-sized steps: initial project setup, Merge Sort, Quick Sort, Median of Medians, and the final Closest Pair/experiments/report update.
