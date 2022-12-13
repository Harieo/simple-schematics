package net.harieo.schematics.animation.serialization;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.animation.impl.basic.EmptyTransition;
import net.harieo.schematics.serialization.impl.transition.TransitionJsonDeserializer;
import org.jetbrains.annotations.NotNull;

public class EmptyTransitionDeserializer extends TransitionJsonDeserializer {

	@Override
	public Transition deserializeToSpecific(@NotNull JsonObject serializedObject, long millisecondsBefore,
			long millisecondsAfter) {
		return new EmptyTransition(millisecondsBefore, millisecondsAfter);
	}

	@Override
	public boolean isValidObject(@NotNull JsonObject serializedObject) {
		return parseType(serializedObject).equalsIgnoreCase(EmptyTransition.TYPE);
	}

}
