import org.example.calculators.DistanceCalculator;
import org.example.calculators.DistanceCalculator2D;
import org.example.calculators.DistanceCalculator3D;
import org.example.points.Point;
import org.example.points.Point2D;
import org.example.points.Point3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DistanceCalculator3DTest {
    private void testDistanceCalculation(Point3D point1, Point3D point2, double expected) {
        DistanceCalculator calculator = new DistanceCalculator3D();
        double result = calculator.calculateDistance(point1, point2);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testCalculateDistance3D_LessCoordinates() {
        testDistanceCalculation(new Point3D(3, 4, 0), new Point3D(0, 0, 0), 5.0);
    }

    @Test
    public void testCalculateDistance3D_SamePoint() {
        testDistanceCalculation( new Point3D(0, 0, 0),  new Point3D(0, 0, 0), 0.0);
    }

    @Test
    public void testCalculateDistance3D_PositiveCoordinates() {
        testDistanceCalculation(new Point3D(1, 2, 3), new Point3D(4, 6, 8), 7.071);
    }

    @Test
    public void testCalculateDistance3D_NegativeCoordinates() {
        testDistanceCalculation(new Point3D(-2, -3, -4), new Point3D(-5, -7, -9), 7.071);
    }

    @Test
    public void testCalculateDistance3D_DifferentPointTypes() {
        DistanceCalculator calculator = new DistanceCalculator3D();
        Point point2D = new Point2D(1, 2);
        Point point3D = new Point3D(4, 6, 8);
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDistance(point2D, point3D);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateDistance(point3D, point2D);
        });
    }
    @Test
    public void testSameDistances() {
        DistanceCalculator calculator = new DistanceCalculator3D();
        Point point1 = new Point3D(1, 2, 3);
        Point point2 = new Point3D(3, 4, 5);
        double result1 = calculator.calculateDistance(point1, point2);
        double result2 = calculator.calculateDistance(point2, point1);
        assertEquals(result1, result2);
    }
}
