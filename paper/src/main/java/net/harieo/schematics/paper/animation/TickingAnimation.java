package net.harieo.schematics.paper.animation;

import net.harieo.schematics.animation.Animation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link Animation} which runs on the {@link org.bukkit.scheduler.BukkitScheduler}.
 * <p>
 * Due to the limitations of the speed of the scheduler, the animation will tick at a rate of
 * {@code 500 milliseconds = 1 tick} or {@code 0.5 seconds = 1 tick}.
 */
public class TickingAnimation extends Animation implements Runnable {

    private final Plugin plugin;

    private transient BukkitTask task;

    /**
     * Accepts {@link Plugin} to register this task with the Bukkit scheduler.
     *
     * @param plugin the plugin to pass to the scheduler to register this task
     */
    public TickingAnimation(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Activates this {@link Runnable} {@link Animation} at a rate of 1 tick with a delay of 1 tick.
     */
    @Override
    public void activate() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, this, 1, 1);
    }

    @Override
    public boolean isActivated() {
        return task != null;
    }

    /**
     * Ticks the animation by 500 milliseconds, and cancels the task when the animation is finished
     */
    @Override
    public void run() {
        if (getState() == State.FINISHED) {
            task.cancel();
            task = null;
            return;
        }

        tick(500);
    }

}
