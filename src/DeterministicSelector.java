public class DeterministicSelector {
    private static int maxRecursionDepth;
    private static long comparisons;

    public static int select(int[] array, int k) {
        if (k < 0 || k >= array.length) {
            throw new IllegalArgumentException("k is outside the array");
        }
        maxRecursionDepth = 0;
        comparisons = 0;
        return select(array, 0, array.length - 1, k, 1);
    }

    public static int getLastMaxRecursionDepth() {
        return maxRecursionDepth;
    }

    public static long getLastComparisons() {
        return comparisons;
    }

    private static int select(int[] array, int left, int right, int k, int depth) {
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);
        if (left == right) {
            return array[left];
        }

        int pivot = medianOfMedians(array, left, right, depth);
        int[] middle = partition(array, left, right, pivot);

        if (k < middle[0]) {
            return select(array, left, middle[0] - 1, k, depth + 1);
        }
        if (k > middle[1]) {
            return select(array, middle[1] + 1, right, k, depth + 1);
        }
        return array[k];
    }

    private static int medianOfMedians(int[] array, int left, int right, int depth) {
        int size = right - left + 1;
        if (size <= 5) {
            insertionSort(array, left, right);
            return array[left + size / 2];
        }

        int medianCount = 0;
        for (int start = left; start <= right; start += 5) {
            int end = Math.min(start + 4, right);
            insertionSort(array, start, end);
            int medianIndex = start + (end - start) / 2;
            swap(array, left + medianCount, medianIndex);
            medianCount++;
        }

        int medianIndex = left + medianCount / 2;
        return select(array, left, left + medianCount - 1, medianIndex, depth + 1);
    }

    private static int[] partition(int[] array, int left, int right, int pivot) {
        int smaller = left;
        int current = left;
        int larger = right;

        while (current <= larger) {
            if (array[current] < pivot) {
                comparisons++;
                swap(array, smaller++, current++);
            } else if (array[current] > pivot) {
                comparisons++;
                swap(array, current, larger--);
            } else {
                current++;
            }
        }
        return new int[]{smaller, larger};
    }

    private static void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int value = array[i];
            int j = i - 1;
            while (j >= left) {
                comparisons++;
                if (array[j] <= value) {
                    break;
                }
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = value;
        }
    }

    private static void swap(int[] array, int first, int second) {
        int temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }
}
