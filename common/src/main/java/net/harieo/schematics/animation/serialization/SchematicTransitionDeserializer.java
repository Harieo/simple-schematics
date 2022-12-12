package net.harieo.schematics.animation.serialization;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.harieo.schematics.animation.impl.basic.SchematicTransition;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.impl.transition.TransitionJsonDeserializer;
import org.jetbrains.annotations.NotNull;

public class SchematicTransitionDeserializer extends TransitionJsonDeserializer {

	private final Deserializer<Schematic, JsonObject> schematicJsonDeserializer;

	public SchematicTransitionDeserializer(@NotNull Deserializer<Schematic, JsonObject> schematicJsonDeserializer) {
		this.schematicJsonDeserializer = schematicJsonDeserializer;
	}

	@Override
	public SchematicTransition deserializeToSpecific(@NotNull JsonObject serializedObject, long millisecondsBefore,
			long millisecondsAfter) {
		Schematic schematic = schematicJsonDeserializer.deserialize(serializedObject.getAsJsonObject("schematic"));
		return new SchematicTransition(schematic, millisecondsBefore, millisecondsAfter);
	}

	@Override
	public boolean isValidObject(@NotNull JsonObject serializedObject) {
		JsonElement element = serializedObject.get("schematic");
		if (element.isJsonObject()) {
			return schematicJsonDeserializer.isValidObject(element.getAsJsonObject());
		} else {
			return false;
		}
	}

}
