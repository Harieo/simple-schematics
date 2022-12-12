package net.harieo.schematics.paper.command.transition;

import net.harieo.schematics.animation.impl.basic.SchematicTransition;
import net.harieo.schematics.paper.schematic.SchematicStorage;
import net.harieo.schematics.schematic.Schematic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SchematicTransitionIntent extends TransitionIntent<SchematicTransition> {

    private final SchematicStorage schematicStorage;

    public SchematicTransitionIntent(@NotNull SchematicStorage schematicStorage) {
        this.schematicStorage = schematicStorage;
    }

    @Override
    public String getId() {
        return "schematic";
    }

    @Override
    public SchematicTransition createTransition(@NotNull Player player, String[] arguments) throws IllegalArgumentException {
        if (arguments.length < 1) {
            throw new IllegalArgumentException("Insufficient arguments. Expected: <schematic id> [before:millis] [after:millis]");
        }

        String schematicId = arguments[0];
        Schematic schematic = schematicStorage.getSchematic(schematicId)
                .orElseThrow(() -> new IllegalArgumentException("No schematic exists with id: " + schematicId));

        MillisecondData millisecondData = parseMillisecondData(arguments, 1);
        return new SchematicTransition(schematic, millisecondData.millisecondsBefore(), millisecondData.millisecondsAfter());
    }

}
