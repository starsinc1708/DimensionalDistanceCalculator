package org.core.distance_calculators;

import org.core.points.Point;
import org.core.points.Point2D;

public class DistanceCalculator2D implements DistanceCalculator {

    @Override
    public double calculateDistance(Point point1, Point point2) {
        if (point1 instanceof Point2D && point2 instanceof Point2D) {
            Point2D p1 = (Point2D) point1;
            Point2D p2 = (Point2D) point2;
            return calculateDistance2D(p1, p2);
        } else {
            throw new IllegalArgumentException("Incompatible point types");
        }
    }

    private double calculateDistance2D(Point2D p1, Point2D p2) {
        double deltaX = Math.abs(p2.getX() - p1.getX());
        double deltaY = Math.abs(p2.getY() - p1.getY());
        return Math.hypot(deltaX, deltaY);
    }
}
