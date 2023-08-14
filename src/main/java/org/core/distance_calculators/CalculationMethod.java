package org.core.distance_calculators;

public enum CalculationMethod {
    TWO_DIMENSIONAL("2D-distance"),
    THREE_DIMENSIONAL("3D-distance");

    private final String title;

    CalculationMethod(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
