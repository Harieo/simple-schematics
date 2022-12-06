package net.harieo.schematics.paper.modification.registry;

import com.google.gson.JsonObject;
import net.harieo.schematics.paper.modification.BukkitModification;
import net.harieo.schematics.paper.modification.impl.blueprint.BukkitModificationJsonBlueprint;
import net.harieo.schematics.paper.modification.impl.deserializer.BlockModificationJsonDeserializer;
import net.harieo.schematics.paper.modification.impl.deserializer.EntitySpawnModificationJsonDeserializer;
import net.harieo.schematics.paper.modification.impl.serializer.BlockModificationJsonSerializer;
import net.harieo.schematics.paper.modification.impl.serializer.EntitySpawnModificationJsonSerializer;
import net.harieo.schematics.serialization.registry.BlueprintRegistry;

/**
 * A {@link BlueprintRegistry} for {@link BukkitModification} blueprints.
 */
public class BukkitJsonBlueprintRegistry extends BlueprintRegistry<BukkitModification, JsonObject> {

    /**
     * Sets up this registry with a default set of blueprints for modifications in this library.
     */
    public BukkitJsonBlueprintRegistry() {
        super(new BukkitModificationJsonBlueprint<>
                        (new BlockModificationJsonSerializer(), new BlockModificationJsonDeserializer()),
                new BukkitModificationJsonBlueprint<>
                        (new EntitySpawnModificationJsonSerializer(), new EntitySpawnModificationJsonDeserializer())
        );
    }

}
