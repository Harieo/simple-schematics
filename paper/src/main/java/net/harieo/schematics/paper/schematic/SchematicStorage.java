package net.harieo.schematics.paper.schematic;

import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import net.harieo.schematics.paper.modification.registry.BukkitJsonBlueprintRegistry;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.paper.position.BukkitJsonCoordinateBlueprint;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Blueprint;
import net.harieo.schematics.serialization.impl.schematic.SchematicJsonDeserializer;
import net.harieo.schematics.serialization.impl.schematic.SchematicJsonSerializer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A handler for reading and writing schematic files in storage.
 */
public class SchematicStorage {

    // Static constants
    public static String DEFAULT_SUBDIRECTORY_NAME = "schematics";
    // Generates filenames as: <schematic id>.json
    public static Function<Schematic, String> DEFAULT_FILENAME_GENERATOR = schematic -> schematic.getId()
            .orElseThrow(() -> new IllegalStateException("Schematic must have id to be saved")) + ".json";

    // Fields for file management
    private final Blueprint<Schematic, JsonObject> schematicJsonBlueprint;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); // For file writing in pretty format

    // Cache
    private final Set<Schematic> schematics = new HashSet<>();

    /**
     * Constructs this storage instance with a {@link Blueprint} of {@link Schematic} to permit serialization and deserialization
     * from JSON for file writing and reading.
     *
     * @param schematicJsonBlueprint for serializing and deserializing schematics
     */
    public SchematicStorage(@NotNull Blueprint<Schematic, JsonObject> schematicJsonBlueprint) {
        this.schematicJsonBlueprint = schematicJsonBlueprint;
    }

    /**
     * Constructs this storage instance with a pair of default {@link SchematicJsonSerializer} and
     * {@link SchematicJsonDeserializer} instances based on {@link BukkitCoordinate} with the default serializer and deserializer from
     * {@link BukkitJsonCoordinateBlueprint} as arguments.
     *
     * @param bukkitJsonBlueprintRegistry the blueprint registry for modifications
     */
    public SchematicStorage(@NotNull BukkitJsonBlueprintRegistry bukkitJsonBlueprintRegistry) {
        BukkitJsonCoordinateBlueprint bukkitJsonCoordinateBlueprint = new BukkitJsonCoordinateBlueprint();
        SchematicJsonSerializer<BukkitCoordinate> schematicJsonSerializer = new SchematicJsonSerializer<>
                (bukkitJsonCoordinateBlueprint.getSerializer());
        SchematicJsonDeserializer<BukkitCoordinate> schematicJsonDeserializer = new SchematicJsonDeserializer<>
                (bukkitJsonCoordinateBlueprint.getDeserializer());
        bukkitJsonBlueprintRegistry.getBlueprints().forEach(schematicJsonDeserializer::addModificationBlueprint);
        // A serializer is created by default in the blueprint, so we do not need to create one for this constructor
        this.schematicJsonBlueprint = new Blueprint<>(schematicJsonSerializer, schematicJsonDeserializer);
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
        plugin.getLogger().info("Loading schematics from subdirectory " + subDirectoryName + "...");
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
                    schematics.add(schematicJsonBlueprint.deserialize(serializedSchematic));
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
     * @param plugin         the plugin managing the schematic files
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

    /**
     * @return the schematic blueprint for deserializing schematics to JSON
     */
    public Blueprint<Schematic, JsonObject> getSchematicJsonBlueprint() {
        return schematicJsonBlueprint;
    }

    /**
     * @return an immutable set of all cached schematics
     */
    public @Unmodifiable Set<Schematic> getSchematics() {
        return ImmutableSet.copyOf(schematics);
    }

    /**
     * Finds a {@link Schematic} from its unique identifier.
     *
     * @param schematicId the identifier for the schematic to be found
     * @return optionally the matching schematic, if present
     */
    public Optional<Schematic> getSchematic(@NotNull String schematicId) {
        return getSchematics().stream()
                .filter(schematic -> schematic.getId().map(id -> id.equals(schematicId)).orElse(false))
                .findAny();
    }

    /**
     * Adds a schematic to the cache, but does not save it to storage.
     *
     * @param schematic the schematic to cache
     */
    public void addSchematic(@NotNull Schematic schematic) {
        schematics.add(schematic);
    }

    /**
     * Removes a schematic from the cache, but does not edit the file storage.
     *
     * @param schematic the schematic to remove from the cache
     */
    public void removeSchematic(@NotNull Schematic schematic) {
        schematics.remove(schematic);
    }

    /**
     * Saves all schematics cached in this instance.
     *
     * @param plugin                     the plugin managing the schematic files
     * @param subDirectoryName           the subdirectory of the plugin directory where schematics files should be saved
     * @param fileNameGenerationFunction a function to generate the name of the file for each schematic
     * @param overwrite                  whether to overwrite existing files with the same name of a schematic
     * @return true if all schematics were successfully saved, or false if one or more schematics could not be saved.
     * @throws IOException                if the subdirectory does not exist and cannot be created
     * @throws NotDirectoryException      if the subdirectory specified is not a directory
     * @throws FileAlreadyExistsException if overwriting is permitted but a file cannot be overwritten due to an error
     * @apiNote If this method returns false, it will be because overwriting was not permitted and a file with the name
     * already exists.
     */
    public boolean saveAll(@NotNull Plugin plugin,
                           @NotNull String subDirectoryName,
                           @NotNull Function<Schematic, String> fileNameGenerationFunction,
                           boolean overwrite) throws IOException {
        File schematicDirectory = new File(plugin.getDataFolder(), subDirectoryName);
        if (!schematicDirectory.exists() && !schematicDirectory.mkdir()) {
            throw new IOException("Failed to create schematics directory");
        } else if (!schematicDirectory.isDirectory()) {
            throw new NotDirectoryException(schematicDirectory.getAbsolutePath() + " is not a directory");
        }

        boolean allSchematicsSaved = true;
        for (Schematic schematic : schematics) { // Using for loop to include exceptions in 'throws' clause
            // Load the serialized json first so that any exceptions occur before file deletion
            String serializedSchematicJson = gson.toJson(schematicJsonBlueprint.serialize(schematic));

            // Delete file if overwriting
            String fileName = fileNameGenerationFunction.apply(schematic);
            File schematicFile = new File(schematicDirectory, fileName);
            if (schematicFile.exists()) {
                if (overwrite) { // Attempt to delete file for overwrite
                    if (!schematicFile.delete()) {
                        throw new FileAlreadyExistsException("Unable to overwrite schematic file " + schematicFile.getAbsolutePath());
                    }
                } else {
                    allSchematicsSaved = false;
                    continue; // No authority to overwrite file, so this schematic will be ignored
                }
            }

            // Write to file
            try (FileWriter writer = new FileWriter(schematicFile)) {
                writer.write(serializedSchematicJson);
            }
        }

        return allSchematicsSaved;
    }

    /**
     * An overload of {@link #saveAll(Plugin, String, Function, boolean)} where the subdirectory name is
     * {@link #DEFAULT_SUBDIRECTORY_NAME}.
     *
     * @throws IOException see description for super method {@link #saveAll(Plugin, String, Function, boolean)}
     */
    public boolean saveAll(@NotNull Plugin plugin,
                           @NotNull Function<Schematic, String> fileNameGenerationFunction,
                           boolean overwrite) throws IOException {
        return saveAll(plugin, DEFAULT_SUBDIRECTORY_NAME, fileNameGenerationFunction, overwrite);
    }

    /**
     * An overload of {@link #saveAll(Plugin, String, Function, boolean)} where the subdirectory name is
     * {@link #DEFAULT_SUBDIRECTORY_NAME} and the filename generator is {@link #DEFAULT_FILENAME_GENERATOR}.
     *
     * @throws IOException see description for super method {@link #saveAll(Plugin, String, Function, boolean)}
     */
    public boolean saveAll(@NotNull Plugin plugin, boolean overwrite) throws IOException {
        return saveAll(plugin, DEFAULT_SUBDIRECTORY_NAME, DEFAULT_FILENAME_GENERATOR, overwrite);
    }

}
