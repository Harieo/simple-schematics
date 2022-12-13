package net.harieo.schematics.position;

import net.harieo.schematics.serialization.impl.coordinate.CoordinateJsonBlueprint;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A 3-dimensional coordinate.
 */
public class Coordinate {

    public static final CoordinateJsonBlueprint DEFAULT_JSON_BLUEPRINT = new CoordinateJsonBlueprint();

    protected final double x;
    protected final double y;
    protected final double z;

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

    /**
     * Clones an existing {@link Coordinate} into this object.
     *
     * @param coordinate the coordinate to clone values from
     */
    public Coordinate(@NotNull Coordinate coordinate) {
        this.x = coordinate.x;
        this.y = coordinate.y;
        this.z = coordinate.z;
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
        return new Coordinate(this.x + vector.getX(), this.y + vector.getY(), this.z + vector.getZ());
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

    /**
     * Calculates the {@link Vector} from this coordinate to another coordinate.
     *
     * @param relativeCoordinate the coordinate relative to this coordinate
     * @return the vector from this coordinate to the relative coordinate
     */
    public Vector getRelativeVector(@NotNull Coordinate relativeCoordinate) {
        double x = relativeCoordinate.getX() - getX();
        double y = relativeCoordinate.getY() - getY();
        double z = relativeCoordinate.getZ() - getZ();
        return new Vector(x, y, z);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + ")";
    }

}
