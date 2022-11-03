package net.harieo.schematics.modification;

import com.google.gson.*;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.position.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Modification} which applies based on a {@link Vector} relative to a static {@link Coordinate}.
 */
public class RelativeModification<T extends Modification> extends Modification {

    private final T actualModification;
    private final Vector vector;

    /**
     * A record of a {@link Modification} which applies based on a {@link Vector} relative to a variable {@link Coordinate}.
     *
     * @param actualModification the actual modification which will occur at the relative {@link Coordinate}
     * @param vector the vector to be applied to coordinates to retrieve the actual coordinate for the actual modification
     * @throws IllegalArgumentException if the actual modification provided is another instance of {@link RelativeModification}
     */
    public RelativeModification(@NotNull T actualModification, @NotNull Vector vector) {
        super("relative-modification");
        if (actualModification instanceof RelativeModification) {
            throw new IllegalArgumentException("Inner modification cannot also be relative");
        }
        this.actualModification = actualModification;
        this.vector = vector;
    }

    /**
     * @return the actual {@link Modification} that will be used at the relative coordinates
     */
    public T getActualModification() {
        return actualModification;
    }

    /**
     * @return the {@link Vector} which will show a {@link Coordinate} relative to another static {@link Coordinate}
     */
    public Vector getVector() {
        return vector;
    }

    /**
     * Creates a {@link Coordinate} relative to an initial {@link Coordinate} based on the {@link Vector} stored by this object.
     *
     * @param initialPosition the initial {@link Coordinate} to apply the relative {@link Vector} to
     * @return the resultant {@link Coordinate}
     */
    public Coordinate getRelativeCoordinate(@NotNull Coordinate initialPosition) {
        return initialPosition.applyVector(vector);
    }

    /**
     * Checks whether the modification is available relative to the provided {@link Coordinate}.
     *
     * @param initialPosition the initial {@link Coordinate} to be passed to {@link #getRelativeCoordinate(Coordinate)}
     * @return whether the modification is available at the relative position
     */
    @Override
    public boolean isAvailable(@NotNull Coordinate initialPosition) {
        return actualModification.isAvailable(getRelativeCoordinate(initialPosition));
    }

    /**
     * Applies the modification relative to the provided {@link Coordinate}.
     *
     * @param initialPosition the initial {@link Coordinate} to be passed to {@link #getRelativeCoordinate(Coordinate)}
     */
    @Override
    public void apply(@NotNull Coordinate initialPosition) {
        actualModification.apply(getRelativeCoordinate(initialPosition));
    }

    @Override
    protected void addSerializationData(@NotNull JsonObject serializedObject) {
        serializedObject.add("vector", vector.serializeToJson());
        serializedObject.add("actual-modification", actualModification.serializeToJson());
    }

    /**
     * Deserializes a serialized {@link RelativeModification} by using a {@link ModificationDeserializer} to deserialize the
     * internal actual {@link Modification} and deserializing the {@link Vector} using {@link Coordinate#deserialize(JsonObject)}.
     *
     * @param serializedObject the serialized {@link RelativeModification}
     * @param modificationDeserializer the deserializer for the type of {@link Modification} which the {@link RelativeModification} should contain
     * @return the deserialized {@link RelativeModification} with the matching internal {@link Modification} type
     * @param <T> the type of the actual {@link Modification} contained within the {@link RelativeModification}
     */
    public static <T extends Modification> RelativeModification<T> deserializeJson(@NotNull JsonObject serializedObject,
                                                                                   @NotNull ModificationDeserializer<T> modificationDeserializer) {
        JsonObject serializedActualModification = serializedObject.getAsJsonObject("actual-modification");
        T deserializedModification = modificationDeserializer.deserialize(serializedActualModification);
        Vector deserializedVector = Coordinate.deserialize(serializedObject.getAsJsonObject("vector")).toVector();
        return new RelativeModification<>(deserializedModification, deserializedVector);
    }

}
