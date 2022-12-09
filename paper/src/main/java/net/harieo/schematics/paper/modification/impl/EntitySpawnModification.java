package net.harieo.schematics.paper.modification.impl;

import com.google.gson.JsonObject;
import net.harieo.schematics.paper.modification.BukkitModification;
import net.harieo.schematics.paper.modification.impl.deserializer.EntitySpawnModificationJsonDeserializer;
import net.harieo.schematics.paper.modification.impl.serializer.EntitySpawnModificationJsonSerializer;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.serialization.Blueprint;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link BukkitModification} which spawns an entity at the given {@link BukkitCoordinate}.
 */
public class EntitySpawnModification extends BukkitModification {

    public static final String ENTITY_TYPE_KEY = "entity-type";
    public static final Blueprint<EntitySpawnModification, JsonObject> BLUEPRINT_JSON =
            new Blueprint<>(
                    new EntitySpawnModificationJsonSerializer(),
                    new EntitySpawnModificationJsonDeserializer()
            );

    private final EntityType entityType;

    /**
     * A {@link BukkitModification} which spawns an entity in the given {@link World}.
     *
     * @param world      the Bukkit world in which this modification takes place
     * @param entityType the type of entity to spawn
     */
    public EntitySpawnModification(@NotNull World world, @NotNull EntityType entityType) {
        super("entity-spawn", world);
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public boolean isAvailable(@NotNull BukkitCoordinate bukkitCoordinate) {
        return true;
    }

    @Override
    public void apply(@NotNull BukkitCoordinate bukkitCoordinate) {
        Location location = bukkitCoordinate.toLocation();
        getWorld().spawnEntity(location, entityType);
    }

    @Override
    public Blueprint<? extends BukkitModification, JsonObject> getJsonBlueprint() {
        return BLUEPRINT_JSON;
    }

}
