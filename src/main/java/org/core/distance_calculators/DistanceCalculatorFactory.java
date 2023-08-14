package org.core.distance_calculators;


import org.core.points.Point;
import org.core.points.Point2D;
import org.core.points.Point3D;

public class DistanceCalculatorFactory {

    private CalculationMethod calculationMethod;

    public double calculateDistance(Point point1, Point point2) {
        if (point1 instanceof Point2D && point2 instanceof Point2D) {
            calculationMethod = CalculationMethod.TWO_DIMENSIONAL;
            return new DistanceCalculator2D().calculateDistance(point1, point2);
        } else if (point1 instanceof Point3D && point2 instanceof Point3D) {
            calculationMethod = CalculationMethod.THREE_DIMENSIONAL;
            return new DistanceCalculator3D().calculateDistance(point1, point2);
        } else {
            throw new IllegalArgumentException("Incompatible point types");
        }
    }

    public CalculationMethod getLastCalculationMethod() {
        return calculationMethod;
    }
}
