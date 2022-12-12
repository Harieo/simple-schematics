package net.harieo.schematics.animation;

import net.harieo.schematics.serialization.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A single stage of an {@link Animation} which performs one task and a specific point.
 *
 * @apiNote This is a {@link Runnable} meaning the task is performed on the abstract {@link #run()} in the implementation.
 */
public abstract class Transition implements Runnable {

    private final String id;
    private final long millisecondsBefore;
    private final long millisecondsAfter;

    /**
     * A transition with an amount of milliseconds before it should run and an amount of milliseconds before a proceeding
     * transition should run.
     *
     * @param id an optional id to identify the type of implementing transition
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter the milliseconds before the next transition should run proceeding this one
     */
    public Transition(@Nullable String id, long millisecondsBefore, long millisecondsAfter) {
        this.id = id;
        this.millisecondsBefore = millisecondsBefore;
        this.millisecondsAfter = millisecondsAfter;
    }

    /**
     * An overload of {@link #Transition(String, long, long)} where the id is null.
     *
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter the milliseconds before the next transition should run proceeding this one
     */
    public Transition(long millisecondsBefore, long millisecondsAfter) {
        this(null, millisecondsBefore, millisecondsAfter);
    }

    /**
     * @return the id, if one has been provided
     */
    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    /**
     * @return the milliseconds before this transition should run
     */
    public long getMillisecondsBefore() {
        return millisecondsBefore;
    }

    /**
     * @return whether {@link #getMillisecondsBefore()} is more than 0
     */
    public boolean hasTimeBefore() {
        return getMillisecondsBefore() > 0;
    }

    /**
     * @return the milliseconds before the next transition should run proceeding this one
     */
    public long getMillisecondsAfter() {
        return millisecondsAfter;
    }

    /**
     * @return whether {@link #getMillisecondsAfter()} is more than 0
     */
    public boolean hasTimeAfter() {
        return getMillisecondsAfter() > 0;
    }

    /**
     * @return the amount of milliseconds that it takes to run this animation, including the time proceedings the
     * run event.
     */
    public long getRunTime() {
        return getMillisecondsBefore() + getMillisecondsAfter();
    }

}
