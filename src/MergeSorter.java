public class MergeSorter {
    private static final int INSERTION_SORT_LIMIT = 16;

    public static void sort(int[] array) {
        int[] buffer = new int[array.length];
        mergeSort(array, buffer, 0, array.length - 1);
    }

    private static void mergeSort(int[] array, int[] buffer, int left, int right) {
        if (right - left + 1 <= INSERTION_SORT_LIMIT) {
            insertionSort(array, left, right);
            return;
        }

        int middle = left + (right - left) / 2;
        mergeSort(array, buffer, left, middle);
        mergeSort(array, buffer, middle + 1, right);
        merge(array, buffer, left, middle, right);
    }

    private static void merge(int[] array, int[] buffer, int left, int middle, int right) {
        int first = left;
        int second = middle + 1;
        int position = left;

        while (first <= middle && second <= right) {
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
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = value;
        }
    }
}
