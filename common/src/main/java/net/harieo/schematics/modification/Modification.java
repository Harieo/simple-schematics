package net.harieo.schematics.modification;

import net.harieo.schematics.exception.ModificationException;
import net.harieo.schematics.modification.serialization.json.ModificationJsonBlueprint;
import net.harieo.schematics.modification.serialization.json.ModificationJsonSerializer;
import net.harieo.schematics.position.Coordinate;
import org.jetbrains.annotations.NotNull;

/**
 * A modification which can be applied at a given {@link Coordinate}
 */
public abstract class Modification {

    private final String type;

    public Modification(@NotNull String type) {
        this.type = type;
    }

    /**
     * @return the string which identifies this type of modification
     */
    public String getType() {
        return type;
    }

    /**
     * Whether this modification can be applied at the given {@link Coordinate}.
     *
     * @param coordinate the coordinate where the modification may be applied
     * @return whether this modification can be applied at the given {@link Coordinate}
     * @implNote this method should reflect the result of {@link #apply(Coordinate)}
     */
    public abstract boolean isAvailable(@NotNull Coordinate coordinate);

    /**
     * Applies this modification at the given {@link Coordinate} on the assumption that {@link #isAvailable(Coordinate)} was
     * checked prior to this call.
     *
     * @param coordinate the coordinate to apply the modification at
     * @throws ModificationException if the modification is not available to be applied at the given {@link Coordinate}
     * @implNote if {@link #isAvailable(Coordinate)} returns {@code true} then there should be no {@link ModificationException} thrown by this method
     */
    public abstract void apply(@NotNull Coordinate coordinate) throws ModificationException;

    /**
     * Provides a default JSON blueprint for this modification as a default method of serialization.
     *
     * @return the JSON blueprint for this modification
     * @param <T> the type of the implementing subclass
     */
    public abstract <T extends Modification> ModificationJsonBlueprint<T> getJsonBlueprint();

    /**
     * Serializes this modification a JSON format identified the {@link #type} as a property.
     *
     * @return the serialized JSON
     */
    /*public JsonObject serializeToJson() {
        JsonObject serializedObject = new JsonObject();
        serializedObject.addProperty("type", type);
        addSerializationData(serializedObject);
        return serializedObject;
    }*/

    /**
     * Adds the data from the implementing class to the serialized JSON constructed in {@link #serializeToJson()}.
     *
     * @param serializedObject the serialized object to add data to
     */
    //protected abstract void addSerializationData(@NotNull JsonObject serializedObject);

}
