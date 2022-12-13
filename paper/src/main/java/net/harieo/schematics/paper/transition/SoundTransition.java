package net.harieo.schematics.paper.transition;

import com.google.common.collect.ImmutableSet;
import net.harieo.schematics.animation.Transition;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A transition which plays a Bukkit {@link Sound} when run.
 */
public class SoundTransition extends Transition {

    private final String sound;
    private final Location location;

    private float volume = 1F;
    private float pitch = 1F;

    private final transient Set<Player> listeners = new HashSet<>();

    /**
     * A transition which plays a Bukkit {@link Sound} when run.
     *
     * @param sound the sound to be played
     * @param location the location to play the sound at
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter  the milliseconds before the next transition should run proceeding this one
     * @throws NullPointerException if the provided {@link Location} does not have a {@link World} present.
     */
    public SoundTransition(@NotNull String sound, @NotNull Location location, long millisecondsBefore, long millisecondsAfter) {
        super("sound", millisecondsBefore, millisecondsAfter);
        Objects.requireNonNull(location.getWorld(), "Location must be based in a World");
        this.sound = sound;
        this.location = location;
    }

    /**
     * A clone constructor for this class.
     *
     * @param soundTransition the existing class to copy fields from
     */
    public SoundTransition(@NotNull SoundTransition soundTransition) {
        this(soundTransition.getSound(), soundTransition.getLocation(),
                soundTransition.getMillisecondsBefore(),soundTransition.getMillisecondsAfter());
        this.volume = soundTransition.volume;
        this.pitch = soundTransition.pitch;
        this.listeners.addAll(soundTransition.listeners);
    }

    /**
     * @return the sound which will be played
     */
    public String getSound() {
        return sound;
    }

    /**
     * @return the location which the sound will be played at
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @return the volume which the sound will be played at, default 1.
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Sets the volume of the sound. Default, and maximum, is 1.
     *
     * @param volume the volume of the sound
     */
    public void setVolume(float volume) {
        this.volume = volume;
    }

    /**
     * @return the pitch that the sound will be played at, default 1.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch of the sound. Default, and maximum, is 1.
     *
     * @param pitch the pitch of the sound
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * @return all players marked as listeners
     */
    public @Unmodifiable Set<Player> getListeners() {
        return ImmutableSet.copyOf(listeners);
    }

    /**
     * Adds a player to the set of players listening to the sound.
     *
     * @param player the player listening to the sound
     */
    public void addListener(@NotNull Player player) {
        listeners.add(player);
    }

    /**
     * Removes a player from the set of players listening to the sound.
     *
     * @param player the player no longer listening to the sound
     */
    public void removeListener(@NotNull Player player) {
        listeners.remove(player);
    }

    /**
     * Checks whether this sound will be played to the set of listeners, or simply broadcast in the world.
     * This is contingent on there being at least one listening player.
     *
     * @return whether this sound will be played exclusively to the set of listening players
     */
    public boolean isRestrictedToListeners() {
        return !listeners.isEmpty();
    }

    @Override
    public void run() {
        if (isRestrictedToListeners()) {
            listeners.forEach(player -> player.playSound(location, sound, volume, pitch));
        } else {
            World world = location.getWorld();
            world.playSound(location, sound, volume, pitch);
        }
    }

}
