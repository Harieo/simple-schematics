package net.harieo.schematics.paper.modification.impl.deserializer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.harieo.schematics.paper.modification.BukkitModificationDeserializer;
import net.harieo.schematics.paper.modification.impl.BlockModification;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class BlockModificationDeserializer extends BukkitModificationDeserializer<BlockModification> {

    @Override
    public BlockModification deserialize(@NotNull JsonObject object, @NotNull World world) {
        String serializedMaterial = object.get(BlockModification.MATERIAL_KEY).getAsString();
        try {
            Material material = Material.valueOf(serializedMaterial.toUpperCase(Locale.ROOT));
            return new BlockModification(world, material);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("No such material '" + serializedMaterial + "'", e);
        }
    }

}
