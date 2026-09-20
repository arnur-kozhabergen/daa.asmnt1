import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class QuickSorterTest {
    @Test
    void sortsDifferentArrays() {
        check(new int[]{});
        check(new int[]{7});
        check(new int[]{1, 2, 3, 4, 5});
        check(new int[]{5, 4, 3, 2, 1});
        check(new int[]{4, 2, 4, 1, 2, 4});
        check(new int[]{0, -5, Integer.MAX_VALUE, -1, Integer.MIN_VALUE});
    }

    @Test
    void sortsRandomArrays() {
        Random random = new Random(42);

        for (int size = 1; size <= 200; size++) {
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(1000);
            }
            check(array);
        }
    }

    private void check(int[] array) {
        int[] expected = array.clone();
        Arrays.sort(expected);

        QuickSorter.sort(array);

        assertArrayEquals(expected, array);
    }
}
