package net.harieo.schematics.paper.modification;

import com.google.gson.JsonObject;
import net.harieo.schematics.exception.ModificationException;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.serialization.Blueprint;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * A platform-specific {@link Modification} which adds {@link World} to allow a default {@link Coordinate} to be converted
 * into a {@link BukkitCoordinate}, so the {@link Modification} can be used within Bukkit/Spigot implementations.
 */
public abstract class BukkitModification extends Modification {

    private final World world;

    /**
     * A {@link Modification} which takes place in the given {@link World}.
     *
     * @param type the identifier for the implementing modification
     * @param world the Bukkit world in which this modification takes place
     */
    public BukkitModification(@NotNull String type, @NotNull World world) {
        super(type);
        this.world = world;
    }

    /**
     * @return the world in which this modification takes place
     */
    public World getWorld() {
        return world;
    }

    @Override
    public boolean isAvailable(@NotNull Coordinate coordinate) {
        return isAvailable(toBukkit(coordinate));
    }

    /**
     * Whether this modification is available at the given {@link BukkitCoordinate}.
     *
     * @param bukkitCoordinate the platform-specific coordinate
     * @return whether this modification is available at that coordinate
     */
    public abstract boolean isAvailable(@NotNull BukkitCoordinate bukkitCoordinate);

    @Override
    public void apply(@NotNull Coordinate coordinate) throws ModificationException {
        apply(toBukkit(coordinate));
    }

    /**
     * Applies this modification at the given {@link BukkitCoordinate}.
     *
     * @param bukkitCoordinate the platform-specific coordinate
     */
    public abstract void apply(@NotNull BukkitCoordinate bukkitCoordinate);

    /**
     * Converts an API {@link Coordinate} into a platform compatible {@link BukkitCoordinate} using the {@link World}
     * provided to this object.
     *
     * @param coordinate the api coordinate
     * @return the platform-specific coordinate
     */
    protected BukkitCoordinate toBukkit(@NotNull Coordinate coordinate) {
        return new BukkitCoordinate(world, coordinate);
    }

    public abstract Blueprint<? extends BukkitModification, JsonObject> getJsonBlueprint();

}
