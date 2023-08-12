import org.example.calculators.DistanceCalculator;
import org.example.calculators.DistanceCalculator2D;
import org.example.points.Point;
import org.example.points.Point2D;
import org.example.points.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DistanceCalculator2DTest {
    private void testDistanceCalculation(Point2D point1, Point2D point2, double expected) {
        DistanceCalculator calculator = new DistanceCalculator2D();
        double result = calculator.calculateDistance(point1, point2);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testCalculateDistance2D() {
        testDistanceCalculation(new Point2D(0, 0), new Point2D(3, 4), 5.0);
    }

    @Test
    public void testCalculateDistance2D_SamePoint() {
        testDistanceCalculation(new Point2D(0, 0), new Point2D(0, 0), 0.0);
    }

    @Test
    public void testCalculateDistance2D_PositiveCoordinates() {
        testDistanceCalculation(new Point2D(1, 2), new Point2D(4, 6), 5.0);
    }

    @Test
    public void testCalculateDistance2D_NegativeCoordinates() {
        testDistanceCalculation(new Point2D(-2, -3),  new Point2D(-5, -7), 5.0);
    }

    @Test
    public void testCalculateDistance2D_DifferentPointTypes() {
        DistanceCalculator2D calculator = new DistanceCalculator2D();
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDistance(new Point2D(1, 2), new Point3D(4, 6, 8));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDistance(new Point3D(4, 6, 8), new Point2D(1, 2));
        });
    }

    @Test
    public void testSameDistances() {
        DistanceCalculator calculator = new DistanceCalculator2D();
        Point point1 = new Point2D(1, 2);
        Point point2 = new Point2D(3, 4);
        double result1 = calculator.calculateDistance(point1, point2);
        double result2 = calculator.calculateDistance(point2, point1);
        assertEquals(result1, result2);
    }
}
