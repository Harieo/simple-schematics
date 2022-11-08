package net.harieo.schematics.modification.serialization.json;

import com.google.gson.JsonObject;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.modification.serialization.ModificationDeserializer;

public abstract class ModificationJsonDeserializer<T extends Modification> implements ModificationDeserializer<T, JsonObject> {

}
