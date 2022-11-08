package net.harieo.schematics.modification;

import net.harieo.schematics.exception.ModificationException;
import net.harieo.schematics.position.Coordinate;
import org.jetbrains.annotations.NotNull;

/**
 * A modification which can be applied at a given {@link Coordinate}
 */
public abstract class Modification {

    private final String type;

    public Modification(@NotNull String type) {
        this.type = type;
    }

    /**
     * @return the string which identifies this type of modification
     */
    public String getType() {
        return type;
    }

    /**
     * Whether this modification can be applied at the given {@link Coordinate}.
     *
     * @param coordinate the coordinate where the modification may be applied
     * @return whether this modification can be applied at the given {@link Coordinate}
     * @implNote this method should reflect the result of {@link #apply(Coordinate)}
     */
    public abstract boolean isAvailable(@NotNull Coordinate coordinate);

    /**
     * Applies this modification at the given {@link Coordinate} on the assumption that {@link #isAvailable(Coordinate)} was
     * checked prior to this call.
     *
     * @param coordinate the coordinate to apply the modification at
     * @throws ModificationException if the modification is not available to be applied at the given {@link Coordinate}
     * @implNote if {@link #isAvailable(Coordinate)} returns {@code true} then there should be no {@link ModificationException} thrown by this method
     */
    public abstract void apply(@NotNull Coordinate coordinate) throws ModificationException;

}
