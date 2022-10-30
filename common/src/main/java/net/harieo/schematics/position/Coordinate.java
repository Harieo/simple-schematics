package net.harieo.schematics.position;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A record of a 3-dimensional coordinate.
 *
 * @param x the x-axis position
 * @param y the y-axis position
 * @param z the z-axis position
 */
public record Coordinate(double x, double y, double z) {

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
