package net.harieo.schematics.position;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Vector(double x, double y, double z) {

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
