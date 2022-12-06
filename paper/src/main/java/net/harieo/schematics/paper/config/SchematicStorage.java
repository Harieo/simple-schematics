package net.harieo.schematics.paper.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.paper.modification.registry.BukkitJsonBlueprintRegistry;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Blueprint;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.impl.schematic.SchematicJsonDeserializer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.HashSet;
import java.util.Set;

/**
 * A handler for reading and writing schematic files in storage.
 */
public class SchematicStorage {

    public static final String DEFAULT_SUBDIRECTORY_NAME = "schematics";

    private final SchematicJsonDeserializer schematicJsonDeserializer;
    private final Set<Schematic> schematics = new HashSet<>();

    /**
     * Constructs this storage instance with a {@link SchematicJsonDeserializer} provided to allow deserialization from
     * storage.
     *
     * @param schematicJsonDeserializer the schematic deserializer which will be able to deserialize schematic files
     */
    public SchematicStorage(@NotNull SchematicJsonDeserializer schematicJsonDeserializer) {
        this.schematicJsonDeserializer = schematicJsonDeserializer;
    }

    /**
     * Constructs this storage instance with a {@link SchematicJsonDeserializer} formed from all the deserializers
     * in {@link BukkitJsonBlueprintRegistry} for deserialization modifications from storage.
     *
     * @param bukkitJsonBlueprintRegistry the blueprint registry for modifications
     */
    @SuppressWarnings("unchecked")
    public SchematicStorage(@NotNull BukkitJsonBlueprintRegistry bukkitJsonBlueprintRegistry) {
        this(new SchematicJsonDeserializer());
        bukkitJsonBlueprintRegistry.getBlueprints().stream()
                .map(Blueprint::getDeserializer)
                .map(deserializer -> (Deserializer<Modification, JsonObject>) deserializer)
                .forEach(schematicJsonDeserializer::addModificationDeserializer);
    }

    /**
     * Loads all schematic files located in a subdirectory of the plugin directory.
     *
     * @param plugin           the plugin managing the schematic files
     * @param subDirectoryName the name of the subdirectory where files are stored
     * @param filenameFilter   an optional {@link FilenameFilter} to filter files within the subdirectory
     * @throws IOException           if the subdirectory does not exist and cannot be created
     * @throws NotDirectoryException if the provided subdirectory is not a directory
     */
    public void load(@NotNull Plugin plugin, @NotNull String subDirectoryName, @Nullable FilenameFilter filenameFilter)
            throws IOException {
        File schematicDirectory = new File(plugin.getDataFolder(), subDirectoryName);
        if (!schematicDirectory.exists()) {
            if (schematicDirectory.mkdir()) {
                return; // If we have just created the directory, it must be empty: no point attempting to read files
            } else {
                throw new IOException("Failed to create schematics directory");
            }
        } else if (!schematicDirectory.isDirectory()) {
            throw new NotDirectoryException(schematicDirectory.getAbsolutePath() + " is not a directory");
        }

        File[] schematicFiles = filenameFilter != null ?
                schematicDirectory.listFiles(filenameFilter) : schematicDirectory.listFiles();
        if (schematicFiles == null || schematicFiles.length < 1) {
            return; // Nothing to read
        }

        for (File schematicFile : schematicFiles) {
            if (!schematicFile.isFile()) {
                continue;
            }

            try (FileReader reader = new FileReader(schematicFile)) {
                JsonElement schematicRawElement = JsonParser.parseReader(reader);
                if (schematicRawElement.isJsonObject()) {
                    JsonObject serializedSchematic = schematicRawElement.getAsJsonObject();
                    schematics.add(schematicJsonDeserializer.deserialize(serializedSchematic));
                } else {
                    plugin.getLogger().warning("Unable to deserialize a schematic JSON object in "
                            + schematicFile.getName());
                }
            }
        }

        plugin.getLogger().info("Loaded " + schematics.size() + " schematics from file successfully.");
    }

    /**
     * Loads schematics from the schematic subdirectory within the plugin folder where the subdirectory name is
     * the {@link #DEFAULT_SUBDIRECTORY_NAME}.
     *
     * @param plugin the plugin managing the schematic files
     * @param filenameFilter an optional {@link FilenameFilter} for scanning files
     * @throws IOException see description from super method {@link #load(Plugin, String, FilenameFilter)}
     */
    public void load(@NotNull Plugin plugin, @Nullable FilenameFilter filenameFilter) throws IOException {
        load(plugin, DEFAULT_SUBDIRECTORY_NAME, filenameFilter);
    }

    /**
     * Loads schematics from the schematic subdirectory within the plugin folder where the subdirectory name is
     * the {@link #DEFAULT_SUBDIRECTORY_NAME} and where no {@link FilenameFilter} is required.
     *
     * @param plugin the plugin managing the schematic files
     * @throws IOException see description from super method {@link #load(Plugin, String, FilenameFilter)}
     */
    public void load(@NotNull Plugin plugin) throws IOException {
        load(plugin, DEFAULT_SUBDIRECTORY_NAME, null);
    }

}
