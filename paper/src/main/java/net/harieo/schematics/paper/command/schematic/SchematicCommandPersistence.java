package net.harieo.schematics.paper.command.schematic;

import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.paper.shape.BukkitCuboid;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Stores persistent data between uses of commands.
 */
public class SchematicCommandPersistence {
    
    private final Map<UUID, BukkitCuboid> markedAreaMap = new HashMap<>();

    /**
     * Gets the {@link BukkitCuboid} which the user is actively forming.
     *
     * @param uuid the uuid of the user
     * @return the cuboid which the user is editing, if they are editing one
     */
    public Optional<BukkitCuboid> getCuboid(@NotNull UUID uuid) {
        return Optional.ofNullable(markedAreaMap.get(uuid));
    }

    /**
     * Sets the {@link BukkitCoordinate} of the lower corner of the {@link BukkitCuboid} which the user intends to
     * create. If no cuboid is actively being edited, a new one will be created.
     *
     * @param uuid the uuid of the user
     * @param bukkitCoordinate the coordinate of the lower corner
     */
    public void setLowerCorner(@NotNull UUID uuid, @NotNull BukkitCoordinate bukkitCoordinate) {
        getCuboid(uuid).orElseGet(() -> {
            BukkitCuboid cuboid = new BukkitCuboid(bukkitCoordinate, null);
            markedAreaMap.put(uuid, cuboid);
            return cuboid;
        }).setLowerCorner(bukkitCoordinate);
    }

    /**
     * Sets the {@link BukkitCoordinate} of the upper corner of the {@link BukkitCuboid} which the user intends to
     * create. If no cuboid is actively being edited, a new one will be created.
     *
     * @param uuid the uuid of the user
     * @param bukkitCoordinate the coordinate of the upper corner
     */
    public void setUpperCorner(@NotNull UUID uuid, @NotNull BukkitCoordinate bukkitCoordinate) {
        getCuboid(uuid).orElseGet(() -> {
            BukkitCuboid cuboid = new BukkitCuboid(null, bukkitCoordinate);
            markedAreaMap.put(uuid, cuboid);
            return cuboid;
        }).setUpperCorner(bukkitCoordinate);
    }
    
}
