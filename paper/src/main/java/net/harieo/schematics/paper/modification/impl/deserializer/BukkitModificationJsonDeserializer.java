package net.harieo.schematics.paper.modification.impl.deserializer;

import com.google.gson.JsonObject;
import net.harieo.schematics.serialization.impl.ModificationJsonDeserializer;
import net.harieo.schematics.paper.modification.BukkitModification;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * An JSON deserializer which parses the required values for a {@link BukkitModification}.
 *
 * @param <T> the type of modification being deserialized
 */
public abstract class BukkitModificationJsonDeserializer<T extends BukkitModification> extends ModificationJsonDeserializer<T> {

    /**
     * Parses {@link World} from a serialized JSON object for {@link BukkitModification}, then passes the object to
     * {@link #deserialize(JsonObject, World)} for deserialization into the specific modification object.
     *
     * @param jsonObject the serialized JSON object
     * @return the deserialized object
     * @throws NullPointerException if the parsed {@link World} name {@code returns Bukkit#getWorld(String) == null}
     */
    @Override
    public final T deserialize(@NotNull JsonObject jsonObject) {
        String worldName = jsonObject.get("world").getAsString();
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new NullPointerException("Bukkit modification requires loaded world: '" + worldName + "'");
        }
        return deserialize(jsonObject, world);
    }

    /**
     * Deserialize the modification after the {@link World} has been found.
     *
     * @param object the serialized data
     * @param world the Bukkit world needed to form the base {@link BukkitModification}
     * @return the deserialized modification
     */
    public abstract T deserialize(@NotNull JsonObject object, @NotNull World world);

}
