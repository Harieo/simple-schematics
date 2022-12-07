package net.harieo.schematics.paper.config;

import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.paper.modification.BukkitModification;
import net.harieo.schematics.paper.modification.registry.BukkitJsonBlueprintRegistry;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Blueprint;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.impl.schematic.SchematicJsonBlueprint;
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
    private final SchematicJsonBlueprint schematicJsonBlueprint;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create(); // For file writing in pretty format

    // Cache
    private final Set<Schematic> schematics = new HashSet<>();

    /**
     * Constructs this storage instance with a {@link SchematicJsonBlueprint} to permit serialization and deserialization
     * for file writing and reading.
     *
     * @param schematicJsonBlueprint for serializing and deserializing schematics
     */
    public SchematicStorage(@NotNull SchematicJsonBlueprint schematicJsonBlueprint) {
        this.schematicJsonBlueprint = schematicJsonBlueprint;
    }

    /**
     * <p>
     * Constructs this storage instance with a {@link SchematicJsonDeserializer} formed from all the deserializers
     * in {@link BukkitJsonBlueprintRegistry} for deserializing modifications from storage.
     * </p>
     * <p>
     * A default {@link SchematicJsonSerializer} is then created to form the {@link SchematicJsonBlueprint}
     * required for this class.
     *</p>
     * @param bukkitJsonBlueprintRegistry the blueprint registry for modifications
     */
    @SuppressWarnings("unchecked")
    public SchematicStorage(@NotNull BukkitJsonBlueprintRegistry bukkitJsonBlueprintRegistry) {
        SchematicJsonDeserializer schematicJsonDeserializer = new SchematicJsonDeserializer();
        bukkitJsonBlueprintRegistry.getBlueprints().stream()
                .map(Blueprint::getDeserializer)
                .map(deserializer -> (Deserializer<? extends Modification, JsonObject>) deserializer)
                .forEach(schematicJsonDeserializer::addModificationDeserializer);
        // A serializer is created by default in the blueprint, so we do not need to create one for this constructor
        this.schematicJsonBlueprint = new SchematicJsonBlueprint(schematicJsonDeserializer);
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
     * @param plugin the plugin managing the schematic files
     * @param subDirectoryName the subdirectory of the plugin directory where schematics files should be saved
     * @param fileNameGenerationFunction a function to generate the name of the file for each schematic
     * @param overwrite whether to overwrite existing files with the same name of a schematic
     * @throws IOException if the subdirectory does not exist and cannot be created
     * @throws NotDirectoryException if the subdirectory specified is not a directory
     * @throws FileAlreadyExistsException if overwriting is permitted but a file cannot be overwritten due to an error
     * @return true if all schematics were successfully saved, or false if one or more schematics could not be saved.
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
            String fileName = fileNameGenerationFunction.apply(schematic);
            File schematicFile = new File(schematicDirectory, fileName);
            if (schematicFile.exists()) {
                if (overwrite && !schematicFile.delete()) { // Attempt to delete file for overwrite, exception on failure
                    throw new FileAlreadyExistsException("Unable to overwrite schematic file " + schematicFile.getAbsolutePath());
                } else {
                    allSchematicsSaved = false;
                    continue; // No authority to overwrite file, so this schematic will be ignored
                }
            }

            try (FileWriter writer = new FileWriter(schematicFile)) {
                writer.write(gson.toJson(schematicJsonBlueprint.serialize(schematic)));
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
