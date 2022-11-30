package net.harieo.schematics.shape;

import net.harieo.schematics.position.Coordinate;
import org.jetbrains.annotations.Nullable;

/**
 * A simple {@link Cuboid} which uses the basic {@link Coordinate} to store positions.
 */
public class SimpleCuboid extends Cuboid<Coordinate> {

    /**
     * Creates a cuboid from two corners.
     *
     * @param lowerCorner the {@link Coordinate} of the lower corner of the cuboid, furthest from the upper corner
     * @param upperCorner the {@link Coordinate} of the upper corner of the cuboid, furthest from the lower corner
     */
    public SimpleCuboid(@Nullable Coordinate lowerCorner, @Nullable Coordinate upperCorner) {
        super(lowerCorner, upperCorner);
    }

    @Override
    public Coordinate createCoordinate(double x, double y, double z) {
        return new Coordinate(x, y, z);
    }

}
