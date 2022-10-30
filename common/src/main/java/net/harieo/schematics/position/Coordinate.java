package net.harieo.schematics.position;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A 3-dimensional coordinate.
 */
public class Coordinate {

    private final double x;
    private final double y;
    private final double z;

    /**
     * A 3-dimensional coordinate.
     *
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param z position on the z-axis
     */
    public Coordinate(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * Applies a {@link Vector} to this coordinate by addition.
     *
     * @param vector the vector to apply to this coordinate
     * @return the resultant coordinate
     */
    @Contract(pure = true)
    public Coordinate applyVector(@NotNull Vector vector) {
        return new Coordinate(this.x + vector.x(), this.y + vector.y(), this.z + vector.z());
    }

    /**
     * Converts this coordinate into a {@link Vector} which changes how it should act.
     *
     * @return this coordinate represented with identical values as a vector
     */
    @Contract(pure = true)
    public Vector toVector() {
        return new Vector(x, y, z);
    }

}
