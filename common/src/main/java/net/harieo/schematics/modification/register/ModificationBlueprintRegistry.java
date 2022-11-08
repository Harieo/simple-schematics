package net.harieo.schematics.modification.register;

import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.modification.serialization.ModificationDeserializer;
import net.harieo.schematics.modification.serialization.ModificationBlueprint;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * A registry which stores {@link ModificationBlueprint}, allowing modifications to be deserialized where the initial
 * {@link Modification} subclass is unknown.
 * </p>
 * <p>
 * For example, if you have ExampleModification which you serialize and save to storage, when the cache is cleared on
 * restart, the system will have no way to know how to re-construct ExampleModification from the serialized data.
 * </p>
 * <p>
 * This is solved by {@link ModificationBlueprint} which is a hard-coded instruction alongside your implementation which
 * tells the system how to deserialize the data using a {@link String} key to point to the correct blueprint.
 * </p>
 */
public class ModificationBlueprintRegistry {

    private final Map<String, ModificationBlueprint<? extends Modification>> register = new HashMap<>();

    public void register(@NotNull String key, @NotNull ModificationBlueprint<? extends Modification> modificationBlueprint) {
        register.put(key, modificationBlueprint);
    }

    public void register(@NotNull ModificationBlueprint<? extends Modification> modificationBlueprint) {
        register(modificationBlueprint.modification().getType(), modificationBlueprint);
    }

    public <T extends Modification> void register(@NotNull String key, @NotNull T modification, @NotNull ModificationDeserializer<T> deserializer) {
        register(key, new ModificationBlueprint<>(modification, deserializer));
    }

    public <T extends Modification> void register(@NotNull T modification, @NotNull ModificationDeserializer<T> deserializer) {
        register(new ModificationBlueprint<>(modification, deserializer));
    }

    public Optional<ModificationBlueprint<? extends Modification>> get(@NotNull String key) {
        return Optional.ofNullable(register.get(key));
    }



}
