package net.harieo.schematics.position;

import com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Set;

/**
 * A 2-dimensional line between two points on the same axis.
 */
public class Axis {

    private final double lowerEndpoint;
    private final double upperEndpoint;

    /**
     * Creates a line on a given axis between two points.
     *
     * @param endpoint1 a point on the axis
     * @param endpoint2 another point on the same axis
     * @apiNote The lower endpoint will always be the minimum of the two endpoints, and vice versa for the upper endpoint
     */
    public Axis(double endpoint1, double endpoint2) {
        this.lowerEndpoint = Math.min(endpoint1, endpoint2);
        this.upperEndpoint = Math.max(endpoint1, endpoint2);
    }

    /**
     * @return the lower of the two endpoints
     */
    public double getLowerEndpoint() {
        return lowerEndpoint;
    }

    /**
     * @return the upper of the two endpoints
     */
    public double getUpperEndpoint() {
        return upperEndpoint;
    }

    /**
     * Gets the range of points between the two endpoints with a specified difference between each listed point.
     *
     * @param differenceBetweenPoints the difference between each point listed
     * @return the set of points within the range
     */
    public @Unmodifiable Set<Double> getRange(double differenceBetweenPoints) {
        Set<Double> range = new HashSet<>();
        range.add(lowerEndpoint); // Include lower endpoint

        // Add all points between
        double pointer = lowerEndpoint;
        while ((pointer += differenceBetweenPoints) <= upperEndpoint) {
            range.add(pointer);
        }

        range.add(upperEndpoint); // Guarantee that the upper endpoint is included
        return ImmutableSet.copyOf(range);
    }

    @Override
    public String toString() {
        return "(" + lowerEndpoint + " -> " + upperEndpoint + ")";
    }
}
