package net.harieo.schematics.serialization.registry;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import net.harieo.schematics.serialization.Blueprint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A registry for {@link Blueprint} for the purpose of caching all available {@link Blueprint} instances.
 *
 * @param <T> the type of deserialized object
 * @param <V> the type of serialized object
 * @apiNote This is useful primarily for caching deserializers which can then be checked via
 * {@link net.harieo.schematics.serialization.Deserializer#isValidObject(Object)}
 */
public class BlueprintRegistry<T, V> {

    private final Set<Blueprint<? extends T, V>> blueprints = new HashSet<>();

    /**
     * A new registry for {@link Blueprint} with a collection of initial blueprints.
     *
     * @param initialBlueprints the collection of initial blueprints
     */
    public BlueprintRegistry(@NotNull Collection<Blueprint<? extends T, V>> initialBlueprints) {
        this.blueprints.addAll(initialBlueprints);
    }

    /**
     * A new registry with an optional initial {@link Blueprint} array.
     *
     * @param initialBlueprints an optional array of initial blueprints
     */
    @SafeVarargs
    public BlueprintRegistry(@NotNull Blueprint<? extends T, V>... initialBlueprints) {
        this(Sets.newHashSet(initialBlueprints));
    }

    /**
     * Adds a {@link Blueprint} to the register.
     *
     * @param blueprint the blueprint to add
     */
    public void addBlueprint(@NotNull Blueprint<? extends T, V> blueprint) {
        blueprints.add(blueprint);
    }

    /**
     * Removes a {@link Blueprint} from the register.
     *
     * @param blueprint the blueprint to remove
     */
    public void removeBlueprint(@NotNull Blueprint<? extends T, V> blueprint) {
        blueprints.remove(blueprint);
    }

    /**
     * @return an immutable set of all blueprints in the register
     */
    public @Unmodifiable Set<Blueprint<? extends T, V>> getBlueprints() {
        return ImmutableSet.copyOf(blueprints);
    }
}
