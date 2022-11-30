package net.harieo.schematics.shape;

import com.google.common.collect.ImmutableSet;
import net.harieo.schematics.position.Axis;
import net.harieo.schematics.position.Coordinate;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A 3-dimensional cuboid marked by two {@link Coordinate} at the furthest corners of the area.
 *
 * @param <T> the type of {@link Coordinate} being used
 */
public abstract class Cuboid<T extends Coordinate> {

    private T lowerCorner;
    private T upperCorner;

    /**
     * Creates a cuboid from two corners.
     *
     * @param lowerCorner the {@link Coordinate} of the lower corner of the cuboid, furthest from the upper corner
     * @param upperCorner the {@link Coordinate} of the upper corner of the cuboid, furthest from the lower corner
     */
    public Cuboid(@Nullable T lowerCorner, @Nullable T upperCorner) {
        this.lowerCorner = lowerCorner;
        this.upperCorner = upperCorner;
    }

    /**
     * @return the lower corner {@link Coordinate}, furthest from the upper corner
     */
    public Optional<T> getLowerCorner() {
        return Optional.ofNullable(lowerCorner);
    }

    public void setLowerCorner(@Nullable T lowerCorner) {
        this.lowerCorner = lowerCorner;
    }

    /**
     * @return the upper corner {@link Coordinate}, furthest from the lower corner
     */
    public Optional<T> getUpperCorner() {
        return Optional.ofNullable(upperCorner);
    }

    public void setUpperCorner(@Nullable T upperCorner) {
        this.upperCorner = upperCorner;
    }

    /**
     * Checks whether the required corners for this cuboid are set.
     *
     * @return whether the information required to use this cuboid is present
     */
    public boolean isValid() {
        return lowerCorner != null && upperCorner != null;
    }

    /**
     * @return the x-axis of this cuboid if both corners are present
     */
    public Optional<Axis> getXAxis() {
        if (isValid()) {
            return Optional.of(new Axis(lowerCorner.getX(), upperCorner.getX()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * @return the y-axis of this cuboid if both corners are present
     */
    public Optional<Axis> getYAxis() {
        if (isValid()) {
            return Optional.of(new Axis(lowerCorner.getY(), upperCorner.getY()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * @return the z-axis of this cuboid if both corners are present
     */
    public Optional<Axis> getZAxis() {
        if (isValid()) {
            return Optional.of(new Axis(lowerCorner.getZ(), upperCorner.getZ()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Gets all the coordinates for every point inside this cuboid in all 3 dimensions.
     *
     * @param differenceBetweenPoints the difference between each point in the cuboid
     * @return the set of all coordinates inside the cuboid
     * @throws IllegalStateException if {@link #isValid()} returns false
     * @throws java.util.NoSuchElementException if one of the {@link Axis} is not present
     */
    public @Unmodifiable Set<T> getInnerCoordinates(double differenceBetweenPoints) {
        if (!isValid()) {
            throw new IllegalStateException("Invalid cuboid");
        }

        Set<T> coordinates = new HashSet<>();
        Axis xAxis = getXAxis().orElseThrow();
        Axis yAxis = getYAxis().orElseThrow();
        Axis zAxis = getZAxis().orElseThrow();

        for (double xPoint : xAxis.getRange(differenceBetweenPoints)) {
            for (double yPoint : yAxis.getRange(differenceBetweenPoints)) {
                for (double zPoint : zAxis.getRange(differenceBetweenPoints)) {
                    coordinates.add(createCoordinate(xPoint, yPoint, zPoint));
                }
            }
        }

        return ImmutableSet.copyOf(coordinates);
    }

    /**
     * Creates an instance of the generic coordinate with the provided values.
     *
     * @param x point on the x-axis
     * @param y point on the y-axis
     * @param z point on the z-axis
     * @return the generic coordinate
     */
    public abstract T createCoordinate(double x, double y, double z);

}
