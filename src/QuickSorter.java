import java.util.concurrent.ThreadLocalRandom;

public class QuickSorter {
    public static void sort(int[] array) {
        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(int[] array, int left, int right) {
        while (left < right) {
            int pivotIndex = ThreadLocalRandom.current().nextInt(left, right + 1);
            int pivot = array[pivotIndex];
            int i = left;
            int j = right;

            while (i <= j) {
                while (array[i] < pivot) {
                    i++;
                }
                while (array[j] > pivot) {
                    j--;
                }
                if (i <= j) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    i++;
                    j--;
                }
            }

            if (j - left < right - i) {
                if (left < j) {
                    quickSort(array, left, j);
                }
                left = i;
            } else {
                if (i < right) {
                    quickSort(array, i, right);
                }
                right = j;
            }
        }
    }
}
