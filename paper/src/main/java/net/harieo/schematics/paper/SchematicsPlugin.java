package net.harieo.schematics.paper;

import co.aikar.commands.PaperCommandManager;
import net.harieo.schematics.paper.command.CommandPosition;
import net.harieo.schematics.paper.command.SchematicCommand;
import net.harieo.schematics.paper.config.SchematicStorage;
import net.harieo.schematics.paper.config.SchematicToolConfiguration;
import net.harieo.schematics.paper.modification.registry.BukkitJsonBlueprintRegistry;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * The {@link JavaPlugin} which allows this library to be loaded as a standalone plugin.
 *
 * @apiNote To access this internal class, please use {@link JavaPlugin#getPlugin(Class)}.
 */
public class SchematicsPlugin extends JavaPlugin {

    private SchematicToolConfiguration schematicToolConfiguration;
    private SchematicStorage schematicStorage;

    @Override
    public void onEnable() {
        this.schematicToolConfiguration = new SchematicToolConfiguration();
        this.schematicStorage = new SchematicStorage(new BukkitJsonBlueprintRegistry());

        try {
            schematicToolConfiguration.load(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerCompletion("positions",
                handler -> Arrays.stream(CommandPosition.values())
                        .map(CommandPosition::getId)
                        .collect(Collectors.toSet())
        );

        commandManager.registerCommand(new SchematicCommand(this));
    }

    public SchematicToolConfiguration getSchematicToolConfiguration() {
        return schematicToolConfiguration;
    }

    public SchematicStorage getSchematicStorage() {
        return schematicStorage;
    }

}
