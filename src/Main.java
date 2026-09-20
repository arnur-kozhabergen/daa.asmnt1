public class Main {
    public static void main(String[] args) throws Exception {
        int[] numbers = {8, 3, 5, 1, 9, 2};
        MergeSorter.sort(numbers);
        System.out.println("MergeSort example: " + java.util.Arrays.toString(numbers));

        int[] values = {9, 1, 6, 3, 7, 2};
        int median = DeterministicSelector.select(values, values.length / 2);
        System.out.println("Median-of-Medians example: " + median);

        Point[] points = {new Point(0, 0), new Point(4, 4), new Point(2, 1)};
        System.out.println("Closest pair example: "
                + ClosestPairSolver.findClosestDistance(points));

        int rows = Experiment.run("results/results.csv");
        System.out.println("Experiments completed: " + rows + " rows saved to results/results.csv");
    }
}
