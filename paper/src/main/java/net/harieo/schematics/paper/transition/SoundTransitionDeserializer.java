package net.harieo.schematics.paper.transition;

import com.google.gson.JsonObject;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.paper.position.BukkitJsonCoordinateBlueprint;
import net.harieo.schematics.serialization.impl.transition.TransitionJsonDeserializer;
import org.jetbrains.annotations.NotNull;

public class SoundTransitionDeserializer extends TransitionJsonDeserializer {

	private final BukkitJsonCoordinateBlueprint bukkitJsonCoordinateBlueprint = new BukkitJsonCoordinateBlueprint();

	@Override
	public SoundTransition deserializeToSpecific(@NotNull JsonObject serializedObject, long millisecondsBefore, long millisecondsAfter) {
		String sound = serializedObject.get("sound").getAsString();
		BukkitCoordinate coordinate = bukkitJsonCoordinateBlueprint.deserialize(serializedObject.getAsJsonObject("location"));
		float volume = serializedObject.get("volume").getAsFloat();
		float pitch = serializedObject.get("pitch").getAsFloat();

		SoundTransition soundTransition = new SoundTransition(sound, coordinate.toLocation(), millisecondsBefore, millisecondsAfter);
		soundTransition.setVolume(volume);
		soundTransition.setPitch(pitch);
		return soundTransition;
	}

	@Override
	public boolean isValidObject(@NotNull JsonObject serializedObject) {
		return parseType(serializedObject).equals(SoundTransition.TYPE) &&
				serializedObject.has("sound") && serializedObject.has("location")
				&& serializedObject.has("volume") && serializedObject.has("pitch");
	}

}
