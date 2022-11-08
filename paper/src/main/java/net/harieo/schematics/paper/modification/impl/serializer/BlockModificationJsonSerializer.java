package net.harieo.schematics.paper.modification.impl.serializer;

import com.google.gson.JsonObject;
import net.harieo.schematics.paper.modification.impl.BlockModification;
import org.jetbrains.annotations.NotNull;

public class BlockModificationJsonSerializer extends BukkitModificationJsonSerializer<BlockModification> {

    @Override
    public void addExtraData(@NotNull BlockModification modification, @NotNull JsonObject serializedObject) {
        serializedObject.addProperty(BlockModification.MATERIAL_KEY, modification.getBlockMaterial().name());
    }

}
