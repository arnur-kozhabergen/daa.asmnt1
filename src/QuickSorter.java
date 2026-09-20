import java.util.concurrent.ThreadLocalRandom;

public class QuickSorter {
    private static int maxRecursionDepth;
    private static long comparisons;
    private static long swaps;

    public static void sort(int[] array) {
        maxRecursionDepth = 0;
        comparisons = 0;
        swaps = 0;
        quickSort(array, 0, array.length - 1, 1);
    }

    public static int getLastMaxRecursionDepth() {
        return maxRecursionDepth;
    }

    public static long getLastComparisons() {
        return comparisons;
    }

    private static void quickSort(int[] array, int left, int right, int depth) {
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);
        while (left < right) {
            int pivotIndex = ThreadLocalRandom.current().nextInt(left, right + 1);
            int pivot = array[pivotIndex];
            int i = left;
            int j = right;

            while (i <= j) {
                while (true) {
                    comparisons++;
                    if (array[i] >= pivot) {
                        break;
                    }
                    i++;
                }
                while (true) {
                    comparisons++;
                    if (array[j] <= pivot) {
                        break;
                    }
                    j--;
                }
                if (i <= j) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    swaps++;
                    i++;
                    j--;
                }
            }

            if (j - left < right - i) {
                if (left < j) {
                    quickSort(array, left, j, depth + 1);
                }
                left = i;
            } else {
                if (i < right) {
                    quickSort(array, i, right, depth + 1);
                }
                right = j;
            }
        }
    }
}
