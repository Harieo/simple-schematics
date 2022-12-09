package net.harieo.schematics.animation.impl.basic;

import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.schematic.Schematic;
import org.jetbrains.annotations.NotNull;

/**
 * Applies a {@link Schematic} when run.
 */
public class SchematicTransition extends Transition {

    private final Schematic schematic;

    /**
     * A transition which applies the specified {@link Schematic} when run.
     *
     * @param schematic the schematic to be applied
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter  the milliseconds before the next transition should run proceeding this one
     */
    public SchematicTransition(@NotNull Schematic schematic, long millisecondsBefore, long millisecondsAfter) {
        super(millisecondsBefore, millisecondsAfter);
        this.schematic = schematic;
    }

    /**
     * @return the schematic which this transition intends to apply
     */
    public Schematic getSchematic() {
        return schematic;
    }

    /**
     * Applies the {@link Schematic} with {@link Schematic#apply()}.
     */
    @Override
    public void run() {
        schematic.apply();
    }

}
