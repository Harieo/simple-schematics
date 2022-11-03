package net.harieo.schematics.paper.modification.deserialization;

import com.google.gson.JsonObject;
import net.harieo.schematics.modification.ModificationDeserializer;
import net.harieo.schematics.paper.modification.BukkitModification;
import org.bukkit.Bukkit;
import org.bukkit.World;

public abstract class BukkitModificationDeserializer<T extends BukkitModification> extends ModificationDeserializer<T> {

    @Override
    public final T deserialize(JsonObject jsonObject) {
        String worldName = jsonObject.get("world").getAsString();
        World world = Bukkit.getWorld(worldName);
        return deserialize(jsonObject, world);
    }

    /**
     * Deserialize the modification after the {@link World} has been found.
     *
     * @param object the serialized data
     * @param world the Bukkit world needed to form the base {@link BukkitModification}
     * @return the deserialized modification
     */
    public abstract T deserialize(JsonObject object, World world);

}
