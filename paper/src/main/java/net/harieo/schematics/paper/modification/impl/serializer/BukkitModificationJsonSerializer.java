package net.harieo.schematics.paper.modification.impl.serializer;

import com.google.gson.JsonObject;
import net.harieo.schematics.serialization.impl.modification.ModificationJsonSerializer;
import net.harieo.schematics.paper.modification.BukkitModification;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public abstract class BukkitModificationJsonSerializer<T extends BukkitModification> extends ModificationJsonSerializer<T> {

    @Override
    public void addSerializationData(@NotNull T modification, @NotNull JsonObject serializedObject) {
        World world = modification.getWorld();
        serializedObject.addProperty("world", world.getName());
        addExtraData(modification, serializedObject);
    }

    public abstract void addExtraData(@NotNull T modification, @NotNull JsonObject serializedObject);

}
