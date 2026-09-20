import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Experiment {
    private static final int[] SIZES = {100, 500, 1000, 2000};
    private static final String[] INPUT_TYPES = {
            "Random", "Sorted", "Reverse-sorted", "Duplicate-heavy"
    };
    private static final int REPEATS = 3;

    public static int run(String outputPath) throws FileNotFoundException {
        File outputFile = new File(outputPath);
        File parent = outputFile.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        int rows = 0;
        try (PrintWriter writer = new PrintWriter(outputFile)) {
            writer.println("algorithm,inputType,n,averageTimeNanos,maxRecursionDepth,operations");

            for (String inputType : INPUT_TYPES) {
                for (int size : SIZES) {
                    Result merge = measureArray("MergeSort", inputType, size);
                    write(writer, merge);
                    rows++;

                    Result quick = measureArray("QuickSort", inputType, size);
                    write(writer, quick);
                    rows++;

                    Result select = measureArray("DeterministicSelect", inputType, size);
                    write(writer, select);
                    rows++;

                    Result closest = measurePoints(inputType, size);
                    write(writer, closest);
                    rows++;
                }
            }
        }
        return rows;
    }

    private static Result measureArray(String algorithm, String inputType, int size) {
        int[] source = createArray(size, inputType);
        long totalTime = 0;
        long totalOperations = 0;
        int maxDepth = 0;

        for (int repeat = 0; repeat <= REPEATS; repeat++) {
            int[] copy = source.clone();
            long start = System.nanoTime();
            long currentOperations;
            if (algorithm.equals("MergeSort")) {
                MergeSorter.sort(copy);
                maxDepth = Math.max(maxDepth, MergeSorter.getLastMaxRecursionDepth());
                currentOperations = MergeSorter.getLastComparisons();
            } else if (algorithm.equals("QuickSort")) {
                QuickSorter.sort(copy);
                maxDepth = Math.max(maxDepth, QuickSorter.getLastMaxRecursionDepth());
                currentOperations = QuickSorter.getLastComparisons();
            } else {
                DeterministicSelector.select(copy, size / 2);
                maxDepth = Math.max(maxDepth, DeterministicSelector.getLastMaxRecursionDepth());
                currentOperations = DeterministicSelector.getLastComparisons();
            }
            long elapsed = System.nanoTime() - start;
            if (repeat > 0) {
                totalTime += elapsed;
                totalOperations += currentOperations;
            }
        }

        return new Result(algorithm, inputType, size,
                totalTime / REPEATS, maxDepth, totalOperations / REPEATS);
    }

    private static Result measurePoints(String inputType, int size) {
        Point[] source = createPoints(size, inputType);
        long totalTime = 0;
        long totalChecks = 0;
        int maxDepth = 0;

        for (int repeat = 0; repeat <= REPEATS; repeat++) {
            long start = System.nanoTime();
            ClosestPairSolver.findClosestDistance(source);
            long elapsed = System.nanoTime() - start;
            maxDepth = Math.max(maxDepth, ClosestPairSolver.getLastMaxRecursionDepth());
            totalChecks += ClosestPairSolver.getLastDistanceChecks();
            if (repeat > 0) {
                totalTime += elapsed;
            }
        }

        return new Result("ClosestPair", inputType, size,
                totalTime / REPEATS, maxDepth, totalChecks / REPEATS);
    }

    private static int[] createArray(int size, String inputType) {
        Random random = new Random(1000L + size + inputType.hashCode());
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = inputType.equals("Duplicate-heavy")
                    ? random.nextInt(20)
                    : random.nextInt(1_000_000);
        }
        if (inputType.equals("Sorted") || inputType.equals("Reverse-sorted")) {
            Arrays.sort(array);
            if (inputType.equals("Reverse-sorted")) {
                for (int i = 0; i < size / 2; i++) {
                    int other = size - 1 - i;
                    int temp = array[i];
                    array[i] = array[other];
                    array[other] = temp;
                }
            }
        }
        return array;
    }

    private static Point[] createPoints(int size, String inputType) {
        Random random = new Random(2000L + size + inputType.hashCode());
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            int x = inputType.equals("Duplicate-heavy")
                    ? random.nextInt(20) : random.nextInt(1_000_000);
            int y = inputType.equals("Duplicate-heavy")
                    ? random.nextInt(20) : random.nextInt(1_000_000);
            points[i] = new Point(x, y);
        }
        if (inputType.equals("Sorted") || inputType.equals("Reverse-sorted")) {
            Comparator<Point> byX = Comparator.comparingInt(Point::getX)
                    .thenComparingInt(Point::getY);
            Arrays.sort(points, byX);
            if (inputType.equals("Reverse-sorted")) {
                for (int i = 0; i < size / 2; i++) {
                    int other = size - 1 - i;
                    Point temp = points[i];
                    points[i] = points[other];
                    points[other] = temp;
                }
            }
        }
        return points;
    }

    private static void write(PrintWriter writer, Result result) {
        writer.printf("%s,%s,%d,%d,%d,%d%n", result.algorithm, result.inputType,
                result.size, result.averageTimeNanos, result.maxRecursionDepth,
                result.operations);
    }

    private record Result(String algorithm, String inputType, int size,
                          long averageTimeNanos, int maxRecursionDepth,
                          long operations) {
    }
}
