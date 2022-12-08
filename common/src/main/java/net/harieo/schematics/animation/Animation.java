package net.harieo.schematics.animation;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * A linear sequence of {@link Transition} which occur on a set linear timeline.
 */
public abstract class Animation {

    // Static information
    private final List<Transition> transitions = new ArrayList<>();

    // Transient information
    private transient Queue<Transition> transitionQueue;
    private transient Transition currentTransition;
    private transient long millisecondsToCurrentTransition = 0;
    private transient boolean currentTransitionRun = false;

    /**
     * An animation with an ordered list of transitions.
     *
     * @param orderedTransitions the ordered list of transitions
     */
    public Animation(@NotNull List<Transition> orderedTransitions) {
        this.transitions.addAll(orderedTransitions);
    }

    /**
     * An animation with an ordered array of transitions.
     *
     * @param orderedTransitions the ordered array of transitions
     */
    public Animation(@NotNull Transition... orderedTransitions) {
        this(Arrays.asList(orderedTransitions));
    }

    /**
     * Activates this animation to begin ticking through the queue of transitions.
     */
    public abstract void activate();

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
            throw new IllegalStateException("No transitions left in queue");
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
     * Resets and re-populates this animation queue.
     */
    public void reset() {
        transitionQueue = new ArrayBlockingQueue<>(transitions.size());
        transitionQueue.addAll(transitions);
        currentTransition = null;
        currentTransitionRun = false;
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

}
