package net.harieo.schematics.paper;

import co.aikar.commands.PaperCommandManager;
import net.harieo.schematics.paper.command.CommandPosition;
import net.harieo.schematics.paper.command.SchematicCommand;
import net.harieo.schematics.paper.config.SchematicToolConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SchematicsPlugin extends JavaPlugin {

    private SchematicToolConfiguration schematicToolConfiguration;

    @Override
    public void onEnable() {
        this.schematicToolConfiguration = new SchematicToolConfiguration();

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

}
