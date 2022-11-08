package net.harieo.schematics.paper.modification.impl.deserializer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.harieo.schematics.paper.modification.impl.EntitySpawnModification;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class EntitySpawnModificationJsonDeserializer extends BukkitModificationJsonDeserializer<EntitySpawnModification> {

    @Override
    public EntitySpawnModification deserialize(@NotNull JsonObject object, @NotNull World world) {
        String serializedEntityType = object.get(EntitySpawnModification.ENTITY_TYPE_KEY).getAsString();
        try {
            EntityType entityType = EntityType.valueOf(serializedEntityType.toUpperCase(Locale.ROOT));
            return new EntitySpawnModification(world, entityType);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("No such entity type '" + serializedEntityType + "'", e);
        }
    }

}
