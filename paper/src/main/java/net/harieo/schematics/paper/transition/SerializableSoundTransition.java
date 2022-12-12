package net.harieo.schematics.paper.transition;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.animation.serialization.TransitionJsonSerializable;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.paper.position.BukkitJsonCoordinateBlueprint;
import net.harieo.schematics.serialization.Serializer;
import net.harieo.schematics.serialization.impl.transition.TransitionJsonSerializer;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

/**
 * An extension of {@link SoundTransition} which is serializable.
 */
public class SerializableSoundTransition extends SoundTransition implements TransitionJsonSerializable {

    private final SoundTransitionJsonSerializer serializer = new SoundTransitionJsonSerializer();

    /**
     * A transition which plays a Bukkit {@link Sound} when run.
     *
     * @param sound              the sound to be played
     * @param location           the location to play the sound at
     * @param millisecondsBefore the milliseconds before this transition should run
     * @param millisecondsAfter  the milliseconds before the next transition should run proceeding this one
     * @throws NullPointerException if the provided {@link Location} does not have a {@link World} present.
     */
    public SerializableSoundTransition(@NotNull String sound, @NotNull Location location,
                                       long millisecondsBefore, long millisecondsAfter) {
        super(sound, location, millisecondsBefore, millisecondsAfter);
    }

    /**
     * A constructor to make {@link SoundTransition} into {@link SerializableSoundTransition}.
     *
     * @param soundTransition the basic transition to copy values from
     */
    public SerializableSoundTransition(@NotNull SoundTransition soundTransition) {
        super(soundTransition);
    }

    @Override
    public @NotNull Serializer<Transition, JsonObject> getSerializer() {
        return serializer;
    }

    public static class SoundTransitionJsonSerializer extends TransitionJsonSerializer {

        private final BukkitJsonCoordinateBlueprint bukkitJsonCoordinateBlueprint = new BukkitJsonCoordinateBlueprint();

        @Override
        public void addExtraData(@NotNull Transition transition, @NotNull JsonObject serializedObject) {
            if (transition instanceof SoundTransition soundTransition) {
                serializedObject.addProperty("sound", soundTransition.getSound());
                serializedObject.add("location", bukkitJsonCoordinateBlueprint.serialize(
                        new BukkitCoordinate(soundTransition.getLocation())));
                serializedObject.addProperty("volume", soundTransition.getVolume());
                serializedObject.addProperty("pitch", soundTransition.getPitch());
            } else {
                throw new IllegalArgumentException("Transition must be an instance of SoundTransition");
            }
        }

    }

}
