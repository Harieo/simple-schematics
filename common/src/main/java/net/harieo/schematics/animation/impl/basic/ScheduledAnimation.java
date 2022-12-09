package net.harieo.schematics.animation.impl.basic;

import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.animation.Transition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * An {@link Animation} which runs on Java's native {@link ScheduledExecutorService} every millisecond.
 */
public class ScheduledAnimation extends Animation implements Runnable {

    private final transient ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private transient ScheduledFuture<?> scheduledFuture;

    public ScheduledAnimation(@Nullable String id, @NotNull List<Transition> transitions) {
        super(id, transitions);
    }

    public ScheduledAnimation(@Nullable String id, @NotNull Transition... transitions) {
        super(id, transitions);
    }

    /**
     * Activates the scheduler to run this {@link Runnable} at a rate of one {@link #run()} per one millisecond.
     */
    @Override
    public void activate() {
        scheduledFuture = executorService.scheduleAtFixedRate(this, 1, 1, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isActivated() {
        return scheduledFuture != null;
    }

    @Override
    public void run() {
        if (getState() == State.FINISHED) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
            return;
        }

        tick(1);
    }

}
