package net.harieo.schematics.animation;

import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * A linear sequence of {@link Transition} which occur on a set linear timeline.
 */
public abstract class Animation {

    // Static information
    private String id;
    private final List<Transition> transitions = new ArrayList<>();

    // Transient information
    private transient Queue<Transition> transitionQueue;
    private transient Transition currentTransition;
    private transient long millisecondsToCurrentTransition = 0;
    private transient boolean currentTransitionRun = false;

    /**
     * An animation with an ordered list of transitions.
     *
     * @param id an optional identifier for this animation
     * @param orderedTransitions the ordered list of transitions
     */
    public Animation(@Nullable String id, @NotNull List<Transition> orderedTransitions) {
        this.id = id;
        this.transitions.addAll(orderedTransitions);
        reset();
    }

    /**
     * An animation with an ordered array of transitions.
     *
     * @param id an optional identifier for this animation
     * @param orderedTransitions the ordered array of transitions
     */
    public Animation(@Nullable String id, @NotNull Transition... orderedTransitions) {
        this(id, Arrays.asList(orderedTransitions));
    }

    /**
     * @return the optional id for this animation, if one is present
     */
    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    /**
     * Sets the id of this animation.
     *
     * @param id the id to be set
     */
    public void setId(@Nullable String id) {
        this.id = id;
    }

    /**
     * @return an unmodifiable list of all transitions for this animation
     */
    public @Unmodifiable List<Transition> getAllTransitions() {
        return ImmutableList.copyOf(transitions);
    }

    /**
     * Adds a transition to the list of transitions.
     *
     * @param transition the transition to add
     */
    public void addTransition(@NotNull Transition transition) {
        transitions.add(transition);
    }

    /**
     * Removes a transition from the list of transitions.
     *
     * @param transition the transition to remove
     */
    public void removeTransition(@NotNull Transition transition) {
        transitions.remove(transition);
    }

    /**
     * Removes a transition from the list of transitions by its index.
     *
     * @param index the index of the transition in the list to remove
     */
    public void removeTransition(int index) {
        transitions.remove(index);
    }

    /**
     * Activates this animation to begin ticking through the queue of transitions.
     */
    public abstract void activate();

    /**
     * @return whether this animation is currently activated and running.
     */
    public abstract boolean isActivated();

    /**
     * @return the total amount of time in milliseconds to perform this entire animation, start to finish.
     */
    public long getTotalAnimationTime() {
        return mapToLong(transitions);
    }

    /**
     * @return the amount of time in milliseconds remaining to perform this animation.
     */
    public long getRemainingAnimationTime() {
        return mapToLong(transitionQueue);
    }

    /**
     * @return the amount of time in milliseconds for the current transition to be completed
     */
    public long getTimeToNextTransition() {
        long totalTransitionTime = currentTransition != null ?
                currentTransition.getMillisecondsBefore() + currentTransition.getMillisecondsAfter() : 0;
        return totalTransitionTime - millisecondsToCurrentTransition;
    }

    /**
     * @return the current transition being run, if one is present
     */
    public Optional<Transition> getCurrentTransition() {
        return Optional.ofNullable(currentTransition);
    }

    /**
     * Peeks the transition queue to see what the next transition is.
     *
     * @return the next transition in the queue, if there is one
     */
    public Optional<Transition> peekNextTransition() {
        return Optional.ofNullable(transitionQueue.peek());
    }

    /**
     * Attempts to progress to the next transition in the queue, if there is one.
     *
     * @return true if there is a new transition, or false if the queue is empty
     */
    public boolean nextTransition() {
        this.millisecondsToCurrentTransition = 0;

        Transition nextTransition = transitionQueue.poll();
        if (nextTransition != null) {
            currentTransition = nextTransition;
            currentTransitionRun = false;
            return true;
        } else {
            currentTransition = null; // Ensure the final transition doesn't linger as the current one
            return false;
        }
    }

    /**
     * Progresses this animation by a certain amount of milliseconds.
     *
     * @param milliseconds the amount of milliseconds which have ticked
     */
    public void tick(long milliseconds) {
        if (currentTransition == null && !nextTransition()) {
            return;
        }

        millisecondsToCurrentTransition += milliseconds;
        if (!currentTransitionRun && millisecondsToCurrentTransition >= currentTransition.getMillisecondsBefore()) {
            currentTransition.run();
            currentTransitionRun = true;
        } else if (currentTransitionRun && millisecondsToCurrentTransition >= currentTransition.getRunTime()) {
            nextTransition();
        }
    }

    /**
     * Resets and re-populates this animation queue. Does nothing if there are no transitions.
     */
    public void reset() {
        if (!transitions.isEmpty()) { // Queue will not accept a size of 0
            transitionQueue = new ArrayBlockingQueue<>(transitions.size());
            transitionQueue.addAll(transitions);
            currentTransition = null;
            currentTransitionRun = false;
        }
    }

    /**
     * Map the total runtime of a collection of {@link Transition}.
     *
     * @param transitions the collection of transition
     * @return the sum of the run times of all transitions in the collection
     */
    private long mapToLong(@NotNull Collection<Transition> transitions) {
        return transitions.stream()
                .mapToLong(Transition::getRunTime)
                .sum();
    }

    /**
     * Ascertains the {@link State} of this animation from the state of its transient values.
     *
     * @return the current state of this animation
     * @apiNote See {@link State} for context on each available state.
     */
    public State getState() {
        if (isActivated()) {
            if (currentTransition == null) {
                if (peekNextTransition().isEmpty()) {
                    return State.FINISHED; // Current transition is null and no further transitions exist
                } else {
                    return State.RUNNING; // Current transition is null but there is a further transition
                }
            } else {
                if (!currentTransitionRun && peekNextTransition().isEmpty()) {
                    return State.ENDING; // The current transition has not run, but it is the last transition
                } else if (currentTransitionRun && peekNextTransition().isEmpty()) {
                    return State.FINISHED; // The current transition has run, and there are no further transitions
                } else {
                    return State.RUNNING; // There are further transitions in the queue
                }
            }
        } else {
            if (currentTransition != null) {
                return State.HALTED; // There is a current transition buffered from the queue, but the animation is deactivated
            } else if (peekNextTransition().isEmpty()) {
                return State.FINISHED; // There is no current transition buffered, and the queue is empty
            } else {
                return State.READY; // There is no current transition, and the queue is populated
            }
        }
    }

    public enum State {
        /**
         * The animation is ready, but not yet activated.
         */
        READY,
        /**
         * The animation is activated, and has at least one transition queued.
         */
        RUNNING,
        /**
         * The animation is activated, but has no more transitions left in the queue.
         */
        ENDING,
        /**
         * The animation is no longer activated and there are no further transitions left in the queue or buffer.
         */
        FINISHED,
        /**
         * The animation is not finished, but it is also not activated.
         */
        HALTED
    }

}
