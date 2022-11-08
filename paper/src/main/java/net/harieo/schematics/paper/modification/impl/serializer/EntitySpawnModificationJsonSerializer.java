package net.harieo.schematics.paper.modification.impl.serializer;

import com.google.gson.JsonObject;
import net.harieo.schematics.paper.modification.impl.EntitySpawnModification;
import org.jetbrains.annotations.NotNull;

public class EntitySpawnModificationJsonSerializer extends BukkitModificationJsonSerializer<EntitySpawnModification> {

    @Override
    public void addExtraData(@NotNull EntitySpawnModification modification, @NotNull JsonObject serializedObject) {
        serializedObject.addProperty(EntitySpawnModification.ENTITY_TYPE_KEY, modification.getEntityType().name());
    }

}
