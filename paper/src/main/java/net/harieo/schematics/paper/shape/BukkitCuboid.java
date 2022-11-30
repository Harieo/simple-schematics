package net.harieo.schematics.paper.shape;

import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.shape.Cuboid;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A {@link Cuboid} which is based in a Bukkit {@link World} by representing coordinates as {@link BukkitCoordinate}.
 */
public class BukkitCuboid extends Cuboid<BukkitCoordinate> {

    private final World world;

    /**
     * Creates a cuboid from two corners.
     *
     * @param lowerCorner the {@link Coordinate} of the lower corner of the cuboid, furthest from the upper corner
     * @param upperCorner the {@link Coordinate} of the upper corner of the cuboid, furthest from the lower corner
     */
    public BukkitCuboid(@NotNull World world, @Nullable Coordinate lowerCorner, @Nullable Coordinate upperCorner) {
        super(lowerCorner != null ? new BukkitCoordinate(world, lowerCorner) : null,
                upperCorner != null ? new BukkitCoordinate(world, upperCorner) : null);
        this.world = world;
    }

    /**
     * Creates a cuboid from two corners in a {@link World} represented by {@link BukkitCoordinate}.
     *
     * @param lowerCorner the lower corner of the cuboid
     * @param upperCorner the upper corner of the cuboid
     * @throws IllegalArgumentException if both coordinates are non-null, and they have different worlds
     * @throws IllegalStateException if neither of the corners can provide a valid {@link World}
     */
    public BukkitCuboid(@Nullable BukkitCoordinate lowerCorner, @Nullable BukkitCoordinate upperCorner) {
        super(lowerCorner, upperCorner);
        if (lowerCorner != null && upperCorner != null && !lowerCorner.getWorld().equals(upperCorner.getWorld())) {
            throw new IllegalArgumentException("Worlds must be identical in cuboid");
        } else if (lowerCorner == null && upperCorner == null) {
            throw new IllegalStateException("One corner must provide a bukkit world");
        } else {
            this.world = Objects.requireNonNullElse(lowerCorner, upperCorner).getWorld();
        }
    }

    /**
     * @return the world which this cuboid is located in
     */
    public World getWorld() {
        return world;
    }

    /**
     * Morphs the result of {@link #getInnerCoordinates(double)} to reflect a set of {@link BukkitCoordinate} based inside
     * the {@link World} stored by this cuboid object.
     *
     * @param differenceBetweenPoints the difference between each point in the cuboid
     * @return the set of coordinates based in a Bukkit {@link World}
     */
    public @Unmodifiable Set<BukkitCoordinate> getInnerBukkitCoordinates(double differenceBetweenPoints) {
        return super.getInnerCoordinates(differenceBetweenPoints)
                .stream()
                .map(coordinate -> new BukkitCoordinate(world, coordinate))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public BukkitCoordinate createCoordinate(double x, double y, double z) {
        return new BukkitCoordinate(world, x, y, z);
    }

}
