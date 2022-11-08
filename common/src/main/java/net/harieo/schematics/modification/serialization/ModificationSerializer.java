package net.harieo.schematics.modification.serialization;

import net.harieo.schematics.modification.Modification;
import org.jetbrains.annotations.NotNull;

public interface ModificationSerializer<T extends Modification, V> {

    V serialize(@NotNull T modification);

}
