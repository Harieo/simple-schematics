package net.harieo.schematics.serialization.impl.transition;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.serialization.Deserializer;
import org.jetbrains.annotations.NotNull;

public abstract class TransitionJsonDeserializer implements Deserializer<Transition, JsonObject> {

	@Override
	public Transition deserialize(@NotNull JsonObject serializedObject) {
		long millisecondsBefore = serializedObject.get("milliseconds-before").getAsLong();
		long millisecondsAfter = serializedObject.get("milliseconds-after").getAsLong();
		return deserializeToSpecific(serializedObject, millisecondsBefore, millisecondsAfter);
	}

	public abstract Transition deserializeToSpecific(@NotNull JsonObject serializedObject, long millisecondsBefore, long millisecondsAfter);

}
