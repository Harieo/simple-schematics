package net.harieo.schematics.animation.impl.basic;

import net.harieo.schematics.animation.Transition;

/**
 * A {@link Transition} which has no action when run to add a delay to the animation sequence.
 */
public class EmptyTransition extends Transition {

    public static final String TYPE = "empty";

    /**
     * A transition with an amount of milliseconds before it should run and an amount of milliseconds before a proceeding
     * transition should run.
     *
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter  the milliseconds before the next transition should run proceeding this one
     */
    public EmptyTransition(long millisecondsBefore, long millisecondsAfter) {
        super(TYPE, millisecondsBefore, millisecondsAfter);
    }

    @Override
    public void run() {
        // No action taken
    }

}
