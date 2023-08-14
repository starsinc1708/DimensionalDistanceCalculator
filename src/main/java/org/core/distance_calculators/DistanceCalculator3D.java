package org.core.distance_calculators;

import org.core.points.Point;
import org.core.points.Point3D;

public class DistanceCalculator3D implements DistanceCalculator {

    @Override
    public double calculateDistance(Point point1, Point point2) {
        if (point1 instanceof Point3D && point2 instanceof Point3D) {
            Point3D p1 = (Point3D) point1;
            Point3D p2 = (Point3D) point2;
            return calculateDistance3D(p1, p2);
        } else {
            throw new IllegalArgumentException("Incompatible point types");
        }
    }

    private double calculateDistance3D(Point3D p1, Point3D p2) {
        double deltaX = Math.abs(p2.getX() - p1.getX());
        double deltaY = Math.abs(p2.getY() - p1.getY());
        double deltaZ = Math.abs(p2.getZ() - p1.getZ());
        double squaredDistance = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
        return Math.sqrt(squaredDistance);
    }
}
