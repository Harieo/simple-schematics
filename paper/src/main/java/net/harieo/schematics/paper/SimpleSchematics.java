package net.harieo.schematics.paper;

import co.aikar.commands.PaperCommandManager;
import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.paper.animation.AnimationStorage;
import net.harieo.schematics.paper.animation.TickingAnimationDeserializer;
import net.harieo.schematics.paper.command.CommandPosition;
import net.harieo.schematics.paper.command.animation.AnimationCommand;
import net.harieo.schematics.paper.command.schematic.SchematicCommand;
import net.harieo.schematics.paper.command.transition.TransitionIntentRegistry;
import net.harieo.schematics.paper.schematic.SchematicStorage;
import net.harieo.schematics.paper.tool.SchematicToolConfiguration;
import net.harieo.schematics.paper.modification.registry.BukkitJsonBlueprintRegistry;
import net.harieo.schematics.paper.tool.SchematicToolListener;
import net.harieo.schematics.schematic.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import net.harieo.schematics.serialization.impl.animation.AnimationJsonSerializer;

/**
 * The {@link JavaPlugin} which allows this library to be loaded as a standalone plugin.
 */
public class SimpleSchematics extends JavaPlugin {

    private static SimpleSchematics INSTANCE;

    private SchematicToolConfiguration schematicToolConfiguration;
    private SchematicStorage schematicStorage;
    private AnimationStorage animationStorage;
    private TransitionIntentRegistry transitionIntentRegistry;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.schematicToolConfiguration = new SchematicToolConfiguration();
        this.schematicStorage = new SchematicStorage(new BukkitJsonBlueprintRegistry());
        this.animationStorage = new AnimationStorage(new AnimationJsonSerializer(),
                new TickingAnimationDeserializer(this, schematicStorage.getSchematicJsonBlueprint()));

        try {
            schematicToolConfiguration.load(this);
            schematicStorage.load(this);
            animationStorage.load(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.transitionIntentRegistry = new TransitionIntentRegistry(this); // Registers default transition intents

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.getCommandCompletions().registerCompletion("positions",
                handler -> Arrays.stream(CommandPosition.values())
                        .map(CommandPosition::getId)
                        .collect(Collectors.toSet())
        );
        commandManager.getCommandCompletions().registerCompletion("schematics",
                handler -> schematicStorage.getSchematics().stream()
                        .map(Schematic::getId)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet()));
        commandManager.getCommandCompletions().registerCompletion("animations",
                handler -> animationStorage.getAnimations().stream()
                        .map(Animation::getId)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toSet()));
        transitionIntentRegistry.registerCommandCompletions("transitions", commandManager);

        SchematicCommand schematicCommand = new SchematicCommand(this);
        commandManager.registerCommand(schematicCommand);
        commandManager.registerCommand(new AnimationCommand(this));

        Bukkit.getPluginManager().registerEvents(new SchematicToolListener(schematicToolConfiguration,
                schematicCommand.getPersistence()), this);
    }

    @Override
    public void onDisable() {
        try {
            getLogger().info("Saving schematics to file...");
            if (schematicStorage.saveAll(this, true)) {
                getLogger().info("Successfully saved all cached schematics.");
            } else {
                getLogger().warning("Failed to save all cached schematics.");
            }

            getLogger().info("Saving animations to file...");
            if (animationStorage.saveAll(this, true)) {
                getLogger().info("Saved all cached animations.");
            } else {
                getLogger().warning("Failed to save all cached animations.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SchematicToolConfiguration getSchematicToolConfiguration() {
        return schematicToolConfiguration;
    }

    public SchematicStorage getSchematicStorage() {
        return schematicStorage;
    }

    public AnimationStorage getAnimationStorage() {
        return animationStorage;
    }

    public TransitionIntentRegistry getTransitionIntentRegistry() {
        return transitionIntentRegistry;
    }

    public static SimpleSchematics get() {
        return INSTANCE;
    }

}
