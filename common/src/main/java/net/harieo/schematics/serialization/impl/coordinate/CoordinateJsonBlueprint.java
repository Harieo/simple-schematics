package net.harieo.schematics.serialization.impl.coordinate;

import com.google.gson.JsonObject;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.serialization.Blueprint;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.Serializer;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Blueprint} for the serialization and deserialization of {@link Coordinate} in JSON format.
 */
public class CoordinateJsonBlueprint extends Blueprint<Coordinate, JsonObject> {

    /**
     * Loads a provided {@link Serializer} and {@link Deserializer} for {@link Coordinate} rather than using the default.
     *
     * @param serializer   the coordinate serializer
     * @param deserializer the coordinate deserializer
     * @apiNote If a custom {@link Serializer} is used, remember that you will need to provide the matching {@link Deserializer}
     * to deserialize your custom-encoded object.
     */
    public CoordinateJsonBlueprint(@NotNull Serializer<Coordinate, JsonObject> serializer,
                                   @NotNull Deserializer<Coordinate, JsonObject> deserializer) {
        super(serializer, deserializer);
    }

    /**
     * Loads the default {@link CoordinateJsonSerializer} and {@link CoordinateJsonDeserializer} into this class.
     */
    public CoordinateJsonBlueprint() {
        this(new CoordinateJsonSerializer(), new CoordinateJsonDeserializer());
    }

    /**
     * A default {@link Serializer} for {@link Coordinate} into JSON.
     */
    public static class CoordinateJsonSerializer implements Serializer<Coordinate, JsonObject> {

        @Override
        public JsonObject serialize(@NotNull Coordinate coordinate) {
            JsonObject serializedObject = new JsonObject();
            serializedObject.addProperty("x", coordinate.getX());
            serializedObject.addProperty("y", coordinate.getY());
            serializedObject.addProperty("z", coordinate.getZ());
            return serializedObject;
        }

    }

    /**
     * A default {@link Deserializer} for {@link Coordinate} from JSON.
     */
    public static class CoordinateJsonDeserializer implements Deserializer<Coordinate, JsonObject> {

        @Override
        public Coordinate deserialize(@NotNull JsonObject serializedCoordinate) {
            double x = serializedCoordinate.get("x").getAsDouble();
            double y = serializedCoordinate.get("y").getAsDouble();
            double z = serializedCoordinate.get("z").getAsDouble();
            return new Coordinate(x, y, z);
        }

    }

}
