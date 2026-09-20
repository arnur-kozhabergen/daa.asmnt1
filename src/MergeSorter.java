public class MergeSorter {
    private static final int INSERTION_SORT_LIMIT = 16;
    private static int maxRecursionDepth;
    private static long comparisons;

    public static void sort(int[] array) {
        maxRecursionDepth = 0;
        comparisons = 0;
        int[] buffer = new int[array.length];
        mergeSort(array, buffer, 0, array.length - 1, 1);
    }

    public static int getLastMaxRecursionDepth() {
        return maxRecursionDepth;
    }

    public static long getLastComparisons() {
        return comparisons;
    }

    private static void mergeSort(int[] array, int[] buffer, int left, int right, int depth) {
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);
        if (right - left + 1 <= INSERTION_SORT_LIMIT) {
            insertionSort(array, left, right);
            return;
        }

        int middle = left + (right - left) / 2;
        mergeSort(array, buffer, left, middle, depth + 1);
        mergeSort(array, buffer, middle + 1, right, depth + 1);
        merge(array, buffer, left, middle, right);
    }

    private static void merge(int[] array, int[] buffer, int left, int middle, int right) {
        int first = left;
        int second = middle + 1;
        int position = left;

        while (first <= middle && second <= right) {
            comparisons++;
            if (array[first] <= array[second]) {
                buffer[position++] = array[first++];
            } else {
                buffer[position++] = array[second++];
            }
        }

        while (first <= middle) {
            buffer[position++] = array[first++];
        }
        while (second <= right) {
            buffer[position++] = array[second++];
        }

        for (int i = left; i <= right; i++) {
            array[i] = buffer[i];
        }
    }

    private static void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int value = array[i];
            int j = i - 1;

            while (j >= left && array[j] > value) {
                comparisons++;
                array[j + 1] = array[j];
                j--;
            }
            if (j >= left) {
                comparisons++;
            }
            array[j + 1] = value;
        }
    }
}
