package net.harieo.schematics.serialization.impl.schematic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.modification.RelativeModification;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Serializer;
import net.harieo.schematics.serialization.impl.coordinate.CoordinateJsonBlueprint;
import net.harieo.schematics.serialization.impl.modification.RelativeModificationJsonBlueprint;
import org.jetbrains.annotations.NotNull;

/**
 * A serializer to JSON format for {@link Schematic}.
 */
public class SchematicJsonSerializer implements Serializer<Schematic, JsonObject> {

    private final Serializer<Coordinate, JsonObject> coordinateJsonSerializer;

    /**
     * A schematic serializer to JSON with a specified {@link Serializer} to serialize coordinates.
     *
     * @param coordinateJsonSerializer the serializer for coordinates
     */
    public SchematicJsonSerializer(@NotNull Serializer<Coordinate, JsonObject> coordinateJsonSerializer) {
        this.coordinateJsonSerializer = coordinateJsonSerializer;
    }

    /**
     * A default schematic serializer to JSON with a default {@link CoordinateJsonBlueprint} to provide the serializer.
     */
    public SchematicJsonSerializer() {
        this(new CoordinateJsonBlueprint().getSerializer());
    }

    @Override
    @SuppressWarnings("unchecked")
    public JsonObject serialize(@NotNull Schematic schematic) {
        JsonObject jsonObject = new JsonObject();
        schematic.getId().ifPresent(id -> jsonObject.addProperty("id", id));
        jsonObject.add("initial-position", coordinateJsonSerializer.serialize(schematic.getInitialPosition()));
        JsonArray modificationArray = new JsonArray();
        schematic.getModifications().stream()
                .map(relativeModification -> (RelativeModification<Modification>) relativeModification)
                .forEach(
                relativeModification -> {
                    RelativeModificationJsonBlueprint<Modification> blueprint = relativeModification.createJsonBlueprint();
                    modificationArray.add(
                            blueprint.serialize(relativeModification)
                    );
                }
        );
        jsonObject.add("modifications", modificationArray);
        return jsonObject;
    }

}
