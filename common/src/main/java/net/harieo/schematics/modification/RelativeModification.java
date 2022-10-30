package net.harieo.schematics.modification;

import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.position.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * A record of a {@link Modification} which applies based on a {@link Vector} relative to a {@link Coordinate}.
 */
public class RelativeModification implements Modification {

    private final Modification actualModification;
    private final Vector vector;

    /**
     * A record of a {@link Modification} which applies based on a {@link Vector} relative to a variable {@link Coordinate}.
     *
     * @param actualModification the actual modification which will occur at the relative {@link Coordinate}
     * @param vector the vector to be applied to coordinates to retrieve the actual coordinate for the actual modification
     */
    public RelativeModification(@NotNull Modification actualModification, @NotNull Vector vector) {
        this.actualModification = actualModification;
        this.vector = vector;
    }

    /**
     * Creates a {@link Coordinate} relative to an initial {@link Coordinate} based on the {@link Vector} stored by this object.
     *
     * @param initialPosition the initial {@link Coordinate} to apply the relative {@link Vector} to
     * @return the resultant {@link Coordinate}
     */
    public Coordinate getRelativeCoordinate(@NotNull Coordinate initialPosition) {
        return initialPosition.applyVector(vector);
    }

    /**
     * Checks whether the modification is available relative to the provided {@link Coordinate}.
     *
     * @param initialPosition the initial {@link Coordinate} to be passed to {@link #getRelativeCoordinate(Coordinate)}
     * @return whether the modification is available at the relative position
     */
    @Override
    public boolean isAvailable(@NotNull Coordinate initialPosition) {
        return actualModification.isAvailable(getRelativeCoordinate(initialPosition));
    }

    /**
     * Applies the modification relative to the provided {@link Coordinate}.
     *
     * @param initialPosition the initial {@link Coordinate} to be passed to {@link #getRelativeCoordinate(Coordinate)}
     */
    @Override
    public void apply(@NotNull Coordinate initialPosition) {
        actualModification.apply(getRelativeCoordinate(initialPosition));
    }
}
