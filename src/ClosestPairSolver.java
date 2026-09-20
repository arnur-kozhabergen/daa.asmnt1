import java.util.Arrays;
import java.util.Comparator;

public class ClosestPairSolver {
    private static int maxRecursionDepth;
    private static long distanceChecks;
    private static final Comparator<Point> BY_X = Comparator
            .comparingInt(Point::getX)
            .thenComparingInt(Point::getY);
    private static final Comparator<Point> BY_Y = Comparator
            .comparingInt(Point::getY)
            .thenComparingInt(Point::getX);

    public static double findClosestDistance(Point[] input) {
        if (input.length < 2) {
            throw new IllegalArgumentException("At least two points are required");
        }

        maxRecursionDepth = 0;
        distanceChecks = 0;
        Point[] points = input.clone();
        Arrays.sort(points, BY_X);
        Point[] buffer = new Point[points.length];
        double squaredDistance = closest(points, buffer, 0, points.length - 1, 1);
        return Math.sqrt(squaredDistance);
    }

    public static int getLastMaxRecursionDepth() {
        return maxRecursionDepth;
    }

    public static long getLastDistanceChecks() {
        return distanceChecks;
    }

    private static double closest(Point[] points, Point[] buffer,
                                  int left, int right, int depth) {
        maxRecursionDepth = Math.max(maxRecursionDepth, depth);
        if (right - left <= 2) {
            double best = bruteForce(points, left, right);
            Arrays.sort(points, left, right + 1, BY_Y);
            return best;
        }

        int middle = left + (right - left) / 2;
        int middleX = points[middle].getX();

        double leftDistance = closest(points, buffer, left, middle, depth + 1);
        double rightDistance = closest(points, buffer, middle + 1, right, depth + 1);
        double best = Math.min(leftDistance, rightDistance);

        mergeByY(points, buffer, left, middle, right);

        int stripSize = 0;
        for (int i = left; i <= right; i++) {
            double dx = (double) points[i].getX() - middleX;
            if (dx * dx < best) {
                buffer[stripSize++] = points[i];
            }
        }

        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize; j++) {
                double dy = (double) buffer[j].getY() - buffer[i].getY();
                if (dy * dy >= best) {
                    break;
                }
                best = Math.min(best, squaredDistance(buffer[i], buffer[j]));
            }
        }
        return best;
    }

    private static void mergeByY(Point[] points, Point[] buffer,
                                 int left, int middle, int right) {
        int i = left;
        int j = middle + 1;
        int position = left;

        while (i <= middle && j <= right) {
            if (BY_Y.compare(points[i], points[j]) <= 0) {
                buffer[position++] = points[i++];
            } else {
                buffer[position++] = points[j++];
            }
        }
        while (i <= middle) {
            buffer[position++] = points[i++];
        }
        while (j <= right) {
            buffer[position++] = points[j++];
        }
        for (int k = left; k <= right; k++) {
            points[k] = buffer[k];
        }
    }

    private static double bruteForce(Point[] points, int left, int right) {
        double best = Double.POSITIVE_INFINITY;
        for (int i = left; i <= right; i++) {
            for (int j = i + 1; j <= right; j++) {
                best = Math.min(best, squaredDistance(points[i], points[j]));
            }
        }
        return best;
    }

    private static double squaredDistance(Point first, Point second) {
        distanceChecks++;
        double dx = (double) first.getX() - second.getX();
        double dy = (double) first.getY() - second.getY();
        return dx * dx + dy * dy;
    }
}
