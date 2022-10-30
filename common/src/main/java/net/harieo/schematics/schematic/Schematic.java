package net.harieo.schematics.schematic;

import com.google.common.collect.ImmutableList;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.modification.RelativeModification;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.position.Vector;
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

    /**
     * A schematic with an optional identifier and a non-optional initial {@link Coordinate} which modifications are
     * relative to.
     *
     * @param id an optional, unique alphanumeric identifier for this schematic
     * @param initialPosition the initial {@link Coordinate}
     * @param initialModifications any initial modifications
     */
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

    /**
     * The initial position of this schematic which all modifications are relative to.
     *
     * @return the coordinate which all modifications will be relative to
     */
    public @NotNull Coordinate getInitialPosition() {
        return initialPosition;
    }

    /**
     * The list of {@link Modification} in the order that they should be applied.
     *
     * @return the list of modifications to create this schematic
     */
    public @NotNull @Unmodifiable List<RelativeModification> getModifications() {
        return ImmutableList.copyOf(modifications);
    }

    /**
     * Adds a {@link RelativeModification} to the list of modifications.
     *
     * @param relativeModification the relative modification
     */
    public void addModification(@NotNull RelativeModification relativeModification) {
        modifications.add(relativeModification);
    }

    /**
     * Adds a generic {@link Modification} which is relative to the initial position by the given {@link Vector}.
     * The generic {@link Modification} is converted to a {@link RelativeModification} using the given {@link Vector} before
     * being added to the list.
     *
     * @param modification the modification to be made
     * @param vector the vector which gives the correct coordinate relative to the schematic's initial coordinate
     */
    public void addModification(@NotNull Modification modification, @NotNull Vector vector) {
        modifications.add(new RelativeModification(modification, vector));
    }

    /**
     * Adds a generic {@link Modification} which will occur exactly at the initial {@link Coordinate} for this schematic.
     * The generic {@link Modification} is still converted to a {@link RelativeModification}, but the {@link Vector} will
     * be (0, 0, 0) to show that no change of position will occur.
     *
     * @param modification the modification to be made
     */
    public void addAbsoluteModification(@NotNull Modification modification) {
        modifications.add(new RelativeModification(modification, new Vector(0, 0, 0)));
    }

    /**
     * Removes a {@link RelativeModification} from the list of modifications.
     *
     * @param modification the object to remove from the list
     */
    public void removeModification(@NotNull RelativeModification modification) {
        modifications.remove(modification);
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
