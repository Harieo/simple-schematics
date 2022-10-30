package net.harieo.schematics.schematic;

import com.google.common.collect.ImmutableList;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.modification.RelativeModification;
import net.harieo.schematics.position.Coordinate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A set of {@link RelativeModification}s which can be sequentially applied based on an initial {@link Coordinate} to create this schematic.
 */
public class Schematic {

    private final String id;
    private final Coordinate initialPosition;
    private final List<RelativeModification> modifications = new ArrayList<>();

    public Schematic(@Nullable String id, @NotNull Coordinate initialPosition, RelativeModification... initialModifications) {
        this.id = id;
        this.initialPosition = initialPosition;
        if (initialModifications.length > 0) {
            modifications.addAll(Arrays.asList(initialModifications));
        }
    }

    /**
     * An optional identifier for this schematic. This is typically used for saving to storage.
     *
     * @return optionally the alphanumeric identifier for this schematic
     */
    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public @NotNull Coordinate getInitialPosition() {
        return initialPosition;
    }

    /**
     * The list of {@link Modification} in the order that they should be applied.
     *
     * @return an immutable list of modifications to create this schematic
     */
    public @NotNull @Unmodifiable List<RelativeModification> getModifications() {
        return ImmutableList.copyOf(modifications);
    }

    /**
     * Calls {@link Modification#apply(Coordinate)} on the list of modifications using the initial position of this schematic.
     *
     * @implNote {@link Modification#isAvailable(Coordinate)} is checked and the modification is only applied if the check
     * returns {@code true}
     */
    public void apply() {
        Coordinate coordinate = getInitialPosition();
        getModifications().forEach(modification -> {
            if (modification.isAvailable(coordinate)) {
                modification.apply(coordinate);
            }
        });
    }

}
