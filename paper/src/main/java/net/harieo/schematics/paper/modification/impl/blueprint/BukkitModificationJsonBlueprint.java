package net.harieo.schematics.paper.modification.impl.blueprint;

import net.harieo.schematics.modification.serialization.json.ModificationJsonBlueprint;
import net.harieo.schematics.paper.modification.BukkitModification;
import net.harieo.schematics.paper.modification.impl.deserializer.BukkitModificationJsonDeserializer;
import net.harieo.schematics.paper.modification.impl.serializer.BukkitModificationJsonSerializer;
import org.jetbrains.annotations.NotNull;

public class BukkitModificationJsonBlueprint<T extends BukkitModification> extends ModificationJsonBlueprint<T> {

    /**
     * An extension of {@link ModificationJsonBlueprint} which works with {@link BukkitModificationJsonSerializer} and
     * {@link BukkitModificationJsonDeserializer}.
     *
     * @param serializer   the method of serialization into JSON
     * @param deserializer the method of deserialization from JSON
     */
    public BukkitModificationJsonBlueprint(@NotNull BukkitModificationJsonSerializer<T> serializer,
                                           @NotNull BukkitModificationJsonDeserializer<T> deserializer) {
        super(serializer, deserializer);
    }

}
