import org.core.distance_calculators.DistanceCalculatorFactory;
import org.core.points.Point;
import org.core.points.Point2D;
import org.core.points.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DistanceCalculatorFactoryTest {
    private void testDistanceCalculation(Point2D point1, Point2D point2, double expected) {
        DistanceCalculatorFactory calculatorFactory = new DistanceCalculatorFactory();
        double result = calculatorFactory.calculateDistance(point1, point2);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testCalculateDistance() {
        testDistanceCalculation(new Point2D(0, 0), new Point2D(3, 4), 5.0);
    }

    @Test
    public void testCalculateDistance_SamePoint() {
        testDistanceCalculation(new Point2D(0, 0), new Point2D(0, 0), 0.0);
    }

    @Test
    public void testCalculateDistance_PositiveCoordinates() {
        testDistanceCalculation(new Point2D(1, 2), new Point2D(4, 6), 5.0);
    }

    @Test
    public void testCalculateDistance_NegativeCoordinates() {
        testDistanceCalculation(new Point2D(-2, -3), new Point2D(-5, -7), 5.0);
    }

    @Test
    public void testCalculateDistance_DifferentPointTypes() {
        DistanceCalculatorFactory calculatorFactory = new DistanceCalculatorFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            calculatorFactory.calculateDistance(new Point2D(1, 2), new Point3D(4, 6, 8));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            calculatorFactory.calculateDistance(new Point3D(4, 6, 8), new Point2D(1, 2));
        });
    }

    @Test
    public void testNullArguments() {
        DistanceCalculatorFactory calculatorFactory = new DistanceCalculatorFactory();
        assertThrows(IllegalArgumentException.class, () -> {
            calculatorFactory.calculateDistance(null, null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            calculatorFactory.calculateDistance(null, new Point2D(1, 2));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            calculatorFactory.calculateDistance(new Point2D(1, 2), null);
        });
    }

    @Test
    public void testSameDistances() {
        DistanceCalculatorFactory calculatorFactory = new DistanceCalculatorFactory();
        Point point1 = new Point2D(1, 2);
        Point point2 = new Point2D(3, 4);
        double result1 = calculatorFactory.calculateDistance(point1, point2);
        double result2 = calculatorFactory.calculateDistance(point2, point1);
        assertEquals(result1, result2);
    }
}
