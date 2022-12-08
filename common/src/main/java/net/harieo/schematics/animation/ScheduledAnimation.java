package net.harieo.schematics.animation;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * An {@link Animation} which runs on Java's native {@link ScheduledExecutorService} every millisecond.
 */
public class ScheduledAnimation extends Animation implements Runnable {

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private transient ScheduledFuture<?> scheduledFuture;

    public ScheduledAnimation(@NotNull List<Transition> transitions) {
        super(transitions);
    }

    public ScheduledAnimation(@NotNull Transition... transitions) {
        super(transitions);
    }

    @Override
    public void activate() {
        scheduledFuture = executorService.scheduleAtFixedRate(this, 1, 1, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (peekNextTransition().isEmpty()) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
            return;
        }

        tick(1);
    }

}
