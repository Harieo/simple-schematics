package net.harieo.schematics.paper.modification.impl;

import net.harieo.schematics.paper.modification.BukkitModification;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link BukkitModification} which spawns an entity at the given {@link BukkitCoordinate}.
 */
public class EntitySpawnModification extends BukkitModification {

    private final EntityType entityType;

    /**
     * A {@link BukkitModification} which spawns an entity in the given {@link World}.
     *
     * @param world the Bukkit world in which this modification takes place
     * @param entityType the type of entity to spawn
     */
    public EntitySpawnModification(@NotNull World world, @NotNull EntityType entityType) {
        super(world);
        this.entityType = entityType;
    }

    @Override
    public boolean isAvailable(@NotNull BukkitCoordinate bukkitCoordinate) {
        return true;
    }

    @Override
    public void apply(@NotNull BukkitCoordinate bukkitCoordinate) {
        Location location = bukkitCoordinate.toLocation();
    }

}
