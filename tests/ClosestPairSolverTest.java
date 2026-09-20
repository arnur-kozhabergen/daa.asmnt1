import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ClosestPairSolverTest {
    @Test
    void findsClosestPair() {
        Point[] points = {
                new Point(0, 0),
                new Point(5, 5),
                new Point(2, 1),
                new Point(2, 2)
        };

        assertEquals(1.0, ClosestPairSolver.findClosestDistance(points), 0.000001);
    }

    @Test
    void returnsZeroForDuplicatePoints() {
        Point[] points = {new Point(3, 4), new Point(3, 4), new Point(10, 10)};

        assertEquals(0.0, ClosestPairSolver.findClosestDistance(points));
    }

    @Test
    void matchesBruteForceOnRandomData() {
        Random random = new Random(42);

        for (int test = 0; test < 100; test++) {
            int size = random.nextInt(99) + 2;
            Point[] points = new Point[size];
            for (int i = 0; i < size; i++) {
                points[i] = new Point(random.nextInt(200) - 100,
                        random.nextInt(200) - 100);
            }

            assertEquals(bruteForce(points),
                    ClosestPairSolver.findClosestDistance(points), 0.000001);
        }

        checkRandomSize(random, 1000);
        checkRandomSize(random, 2000);
    }

    @Test
    void rejectsLessThanTwoPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> ClosestPairSolver.findClosestDistance(new Point[0]));
    }

    private double bruteForce(Point[] points) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double dx = (double) points[i].getX() - points[j].getX();
                double dy = (double) points[i].getY() - points[j].getY();
                best = Math.min(best, Math.sqrt(dx * dx + dy * dy));
            }
        }
        return best;
    }

    private void checkRandomSize(Random random, int size) {
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(random.nextInt(2000), random.nextInt(2000));
        }
        assertEquals(bruteForce(points),
                ClosestPairSolver.findClosestDistance(points), 0.000001);
    }
}
