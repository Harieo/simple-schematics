package net.harieo.schematics.animation;

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
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter  the milliseconds before the next transition should run proceeding this one
     */
    public SchematicTransition(@NotNull Schematic schematic, long millisecondsBefore, long millisecondsAfter) {
        super(millisecondsBefore, millisecondsAfter);
        this.schematic = schematic;
    }

    @Override
    public void run() {
        schematic.apply();
    }

}
