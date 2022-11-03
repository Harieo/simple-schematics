package net.harieo.schematics.paper.modification.deserialization;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.harieo.schematics.paper.modification.impl.BlockModification;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Locale;

public class BlockModificationDeserializer extends BukkitModificationDeserializer<BlockModification> {

    @Override
    public BlockModification deserialize(JsonObject object, World world) {
        String serializedMaterial = object.get(BlockModification.MATERIAL_KEY).getAsString();
        try {
            Material material = Material.valueOf(serializedMaterial.toUpperCase(Locale.ROOT));
            return new BlockModification(world, material);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("No such material '" + serializedMaterial + "'", e);
        }
    }

}
