import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeterministicSelectorTest {
    @Test
    void findsValuesWithDuplicates() {
        int[] array = {7, -2, 7, 4, 1, Integer.MIN_VALUE, Integer.MAX_VALUE};

        for (int k = 0; k < array.length; k++) {
            check(array, k);
        }
    }

    @Test
    void passesRandomTests() {
        Random random = new Random(42);

        for (int test = 0; test < 200; test++) {
            int size = random.nextInt(200) + 1;
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(1000) - 500;
            }
            check(array, random.nextInt(size));
        }
    }

    @Test
    void rejectsInvalidK() {
        assertThrows(IllegalArgumentException.class,
                () -> DeterministicSelector.select(new int[]{1, 2, 3}, -1));
        assertThrows(IllegalArgumentException.class,
                () -> DeterministicSelector.select(new int[]{1, 2, 3}, 3));
    }

    private void check(int[] original, int k) {
        int[] expected = original.clone();
        Arrays.sort(expected);

        int actual = DeterministicSelector.select(original.clone(), k);

        assertEquals(expected[k], actual);
    }
}
