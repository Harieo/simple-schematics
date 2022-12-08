package net.harieo.schematics.animation;

/**
 * A single stage of an {@link Animation} which performs one task and a specific point.
 */
public abstract class Transition {

    private final long millisecondsBefore;
    private final long millisecondsAfter;

    /**
     * A transition with an amount of milliseconds before it should run and an amount of milliseconds before a proceeding
     * transition should run.
     *
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter the milliseconds before the next transition should run proceeding this one
     */
    public Transition(long millisecondsBefore, long millisecondsAfter) {
        this.millisecondsBefore = millisecondsBefore;
        this.millisecondsAfter = millisecondsAfter;
    }

    /**
     * Runs this transition
     */
    public abstract void run();

    /**
     * @return the milliseconds before this transition should run
     */
    public long getMillisecondsBefore() {
        return millisecondsBefore;
    }

    /**
     * @return the milliseconds before the next transition should run proceeding this one
     */
    public long getMillisecondsAfter() {
        return millisecondsAfter;
    }

    /**
     * @return the amount of milliseconds that it takes to run this animation, including the time proceedings the
     * run event.
     */
    public long getRunTime() {
        return getMillisecondsBefore() + getMillisecondsAfter();
    }

}
