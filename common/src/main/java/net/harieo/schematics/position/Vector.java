package net.harieo.schematics.position;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Vector extends Coordinate {

    /**
     * A 3-dimensional vector which, in-effect, is a {@link Coordinate} but has slightly different functions to perform
     * as an offset from a static {@link Coordinate}.
     *
     * @param x the x-axis difference
     * @param y the y-axis difference
     * @param z the z-axis difference
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Adds the provided vector to this vector.
     *
     * @param vector the vector to add to this vector
     * @return the resultant vector
     */
    @Contract(pure = true)
    public Vector add(@NotNull Vector vector) {
        return new Vector(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    /**
     * Subtracts the provided vector from this vector.
     *
     * @param vector the vector to subtract from this vector
     * @return the resultant vector
     */
    @Contract(pure = true)
    public Vector subtract(@NotNull Vector vector) {
        return new Vector(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

}
