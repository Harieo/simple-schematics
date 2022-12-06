package net.harieo.schematics.paper.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.harieo.schematics.schematic.Schematic;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.HashSet;
import java.util.Set;

public class SchematicStorage {

    private final Set<Schematic> schematics = new HashSet<>();

    public void load(@NotNull Plugin plugin) throws IOException {
        File schematicDirectory = new File(plugin.getDataFolder(), "schematics");
        if (!schematicDirectory.exists()) {
            if (schematicDirectory.mkdir()) {
                return; // If we have just created the directory, it must be empty: no point attempting to read files
            } else {
                throw new IOException("Failed to create schematics directory");
            }
        } else if (!schematicDirectory.isDirectory()) {
            throw new NotDirectoryException(schematicDirectory.getAbsolutePath() + " is not a directory");
        }

        File[] schematicFiles = schematicDirectory.listFiles();
        if (schematicFiles == null || schematicFiles.length < 1) {
            return; // Nothing to read
        }

        for (File schematicFile : schematicFiles) {
            try (FileReader reader = new FileReader(schematicFile)) {
                JsonElement schematicRawElement = JsonParser.parseReader(reader);
                // TODO Deserialize raw schematic
            }
        }
    }

}
