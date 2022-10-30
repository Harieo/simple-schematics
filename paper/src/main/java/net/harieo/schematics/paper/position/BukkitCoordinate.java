package net.harieo.schematics.paper.position;

import net.harieo.schematics.position.Coordinate;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class BukkitCoordinate extends Coordinate {

    private final World world;

    /**
     * A 3-dimensional coordinate with a {@link World} to imitate a Bukkit {@link Location}.
     *
     * @param x position on the x-axis
     * @param y position on the y-axis
     * @param z position on the z-axis
     */
    public BukkitCoordinate(@NotNull World world, double x, double y, double z) {
        super(x, y, z);
        this.world = world;
    }

    public BukkitCoordinate(@NotNull World world, @NotNull Coordinate coordinate) {
        super(coordinate.getX(), coordinate.getY(), coordinate.getZ());
        this.world = world;
    }

    /**
     * Converts a Bukkit {@link Location} into a {@link Coordinate}.
     *
     * @param location the Bukkit location
     */
    public BukkitCoordinate(@NotNull Location location) {
        super(location.getX(), location.getY(), location.getZ());
        this.world = location.getWorld();
    }

    public World getWorld() {
        return world;
    }

    /**
     * Converts this coordinate into a Bukkit {@link Location}.
     *
     * @return the Bukkit location
     */
    public @NotNull Location toLocation() {
        return new Location(world, getX(), getY(), getZ());
    }

}
