package net.harieo.schematics.modification.serialization;

import net.harieo.schematics.modification.Modification;
import org.jetbrains.annotations.NotNull;

public interface ModificationDeserializer<T extends Modification, V> {

    T deserialize(@NotNull V serializedObject);

}
