package net.harieo.schematics.paper.position;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.serialization.Blueprint;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.Serializer;
import net.harieo.schematics.serialization.impl.coordinate.CoordinateJsonBlueprint;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * A JSON {@link Blueprint} for {@link BukkitCoordinate}.
 */
public class BukkitJsonCoordinateBlueprint extends Blueprint<BukkitCoordinate, JsonObject> {

    /**
     * Creates this blueprint with a specified serializer and deserializer.
     *
     * @param serializer   the serializer
     * @param deserializer the deserializer
     */
    public BukkitJsonCoordinateBlueprint(@NotNull Serializer<BukkitCoordinate, JsonObject> serializer,
                                         @NotNull Deserializer<BukkitCoordinate, JsonObject> deserializer) {
        super(serializer, deserializer);
    }

    /**
     * Creates this blueprint with the default {@link BukkitJsonCoordinateSerializer} and {@link BukkitJsonCoordinateDeserializer}.
     */
    public BukkitJsonCoordinateBlueprint() {
        this(new BukkitJsonCoordinateSerializer(), new BukkitJsonCoordinateDeserializer());
    }

    /**
     * A {@link Serializer} for {@link BukkitCoordinate}.
     */
    public static class BukkitJsonCoordinateSerializer implements Serializer<BukkitCoordinate, JsonObject> {

        private final Serializer<Coordinate, JsonObject> baseCoordinateSerializer;

        /**
         * Creates a serializer with a base {@link Serializer} for {@link Coordinate} to serialize base values.
         *
         * @param baseCoordinateSerializer the serializer to serialize the base values of {@link Coordinate}
         */
        public BukkitJsonCoordinateSerializer(@NotNull Serializer<Coordinate, JsonObject> baseCoordinateSerializer) {
            this.baseCoordinateSerializer = baseCoordinateSerializer;
        }

        /**
         * Creates a serializer using the default {@link CoordinateJsonBlueprint.CoordinateJsonSerializer}.
         */
        public BukkitJsonCoordinateSerializer() {
            this(new CoordinateJsonBlueprint.CoordinateJsonSerializer());
        }

        @Override
        public JsonObject serialize(@NotNull BukkitCoordinate object) {
            JsonObject serializedBaseCoordinate = baseCoordinateSerializer.serialize(object);
            serializedBaseCoordinate.addProperty("world", object.getWorld().getName());
            return serializedBaseCoordinate;
        }

    }

    /**
     * A {@link Deserializer} for {@link BukkitCoordinate}.
     */
    public static class BukkitJsonCoordinateDeserializer implements Deserializer<BukkitCoordinate, JsonObject> {

        private final Deserializer<Coordinate, JsonObject> baseCoordinateDeserializer;

        /**
         * Creates a deserializer with a base {@link Deserializer} for {@link Coordinate} to deserialize base values.
         *
         * @param baseCoordinateDeserializer the deserializer to deserialize the base values of {@link Coordinate}
         */
        public BukkitJsonCoordinateDeserializer(@NotNull Deserializer<Coordinate, JsonObject> baseCoordinateDeserializer) {
            this.baseCoordinateDeserializer = baseCoordinateDeserializer;
        }

        /**
         * Creates a serializer using the default {@link CoordinateJsonBlueprint.CoordinateJsonDeserializer}.
         */
        public BukkitJsonCoordinateDeserializer() {
            this(new CoordinateJsonBlueprint.CoordinateJsonDeserializer());
        }

        @Override
        public BukkitCoordinate deserialize(@NotNull JsonObject serializedObject) {
            Coordinate baseCoordinate = baseCoordinateDeserializer.deserialize(serializedObject);
            String worldName = serializedObject.get("world").getAsString();
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                throw new JsonParseException("World does not exist: " + worldName);
            }
            return new BukkitCoordinate(world, baseCoordinate);
        }

        @Override
        public boolean isValidObject(@NotNull JsonObject serializedObject) {
            return baseCoordinateDeserializer.isValidObject(serializedObject) && serializedObject.has("world");
        }

    }

}
