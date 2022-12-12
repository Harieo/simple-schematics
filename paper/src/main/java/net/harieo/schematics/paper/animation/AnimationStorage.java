package net.harieo.schematics.paper.animation;

import org.bukkit.plugin.Plugin;

import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NotDirectoryException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.serialization.Deserializer;
import net.harieo.schematics.serialization.Serializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * A handler for reading and writing {@link Animation} to file storage.
 */
public class AnimationStorage {

	public static String DEFAULT_SUBDIRECTORY_NAME = "animations";
	public static Function<Animation, String> DEFAULT_FILENAME_GENERATOR = animation -> animation.getId()
			.orElseThrow(() -> new IllegalStateException("Animation must have id to be saved")) + ".json";

	private final Serializer<Animation, JsonObject> animationJsonSerializer;
	private final Deserializer<? extends Animation, JsonObject> animationJsonDeserializer;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private final Set<Animation> animations = new HashSet<>();

	/**
	 * Takes a {@link Serializer} and {@link Deserializer} for {@link Animation}.
	 *
	 * @param animationJsonSerializer a serializer for animation
	 * @param animationJsonDeserializer a deserializer for animation
	 */
	public AnimationStorage(@NotNull Serializer<Animation, JsonObject> animationJsonSerializer,
			@NotNull Deserializer<? extends Animation, JsonObject> animationJsonDeserializer) {
		this.animationJsonSerializer = animationJsonSerializer;
		this.animationJsonDeserializer = animationJsonDeserializer;
	}

	/**
	 * @return an unmodifiable set of the cached animations
	 */
	public @Unmodifiable Set<Animation> getAnimations() {
		return ImmutableSet.copyOf(animations);
	}

	/**
	 * Adds an animation to the cache, but does not save it.
	 *
	 * @param animation the animation to add to the cache
	 */
	public void addAnimation(@NotNull Animation animation) {
		animations.add(animation);
	}

	/**
	 * Loads all serialized animation JSON files from a subdirectory of the plugin directory.
	 *
	 * @param plugin the plugin which manages the files
	 * @param subdirectory the name of the subdirectory within the plugin directory
	 * @throws IOException if the subdirectory provided is not a directory
	 */
	public void load(@NotNull Plugin plugin, @NotNull String subdirectory) throws IOException {
		File animationDirectory = new File(plugin.getDataFolder(), subdirectory);
		if (!animationDirectory.exists()) {
			return;
		} else if (!animationDirectory.isDirectory()) {
			throw new NotDirectoryException("Animation subdirectory is not a directory: " + subdirectory);
		}

		File[] animationFiles = animationDirectory.listFiles();
		if (animationFiles == null) {
			return;
		}

		for (File animationFile : animationFiles) {
			try (FileReader reader = new FileReader(animationFile)) {
				JsonElement rawAnimationElement = JsonParser.parseReader(reader);
				if (rawAnimationElement.isJsonObject()) {
					JsonObject rawAnimationJson = rawAnimationElement.getAsJsonObject();
					animations.add(animationJsonDeserializer.deserialize(rawAnimationJson));
				}
			}
		}
	}

	/**
	 * An overload of {@link #load(Plugin, String)} which uses {@link #DEFAULT_SUBDIRECTORY_NAME} for the subdirectory
	 * name.
	 *
	 * @param plugin the plugin managing files
	 * @throws IOException see {@link #load(Plugin, String)} for details.
	 */
	public void load(@NotNull Plugin plugin) throws IOException {
		load(plugin, DEFAULT_SUBDIRECTORY_NAME);
	}

	/**
	 * Saves all cached animations to file.
	 *
	 * @param plugin the plugin managing the files
	 * @param subdirectory the name of the subdirectory within the plugin directory
	 * @param fileNamingFunction the function for naming animation files
	 * @param overwrite whether to overwrite existing files
	 * @return whether all animations were saved to file. If overwrite is not permitted, this may return false.
	 * @throws IOException if there is an issue with the subdirectory
	 */
	public boolean saveAll(@NotNull Plugin plugin,
			@NotNull String subdirectory,
			@NotNull Function<Animation, String> fileNamingFunction,
			boolean overwrite) throws IOException {
		File animationDirectory = new File(plugin.getDataFolder(), subdirectory);
		if (!animationDirectory.exists() && !animationDirectory.mkdir()) {
			throw new IOException("Unable to create animation subdirectory");
		} else if (!animationDirectory.isDirectory()) {
			throw new NotDirectoryException("Animation subdirectory is not directory: " + subdirectory);
		}

		boolean savedAll = true;
		for (Animation animation : animations) {
			File animationFile = new File(animationDirectory, fileNamingFunction.apply(animation));
			if (animationFile.exists()) {
				if (overwrite && !animationFile.delete()) {
					throw new FileAlreadyExistsException("Animation file cannot be overwritten: " + animationFile.getAbsolutePath());
				} else if (!overwrite) {
					savedAll = false;
					continue;
				}
			}

			JsonObject serializedAnimation = animationJsonSerializer.serialize(animation);
			try (FileWriter writer = new FileWriter(animationFile)) {
				writer.write(gson.toJson(serializedAnimation));
			}
		}

		return savedAll;
	}

	/**
	 * An overload of {@link #saveAll(Plugin, String, Function, boolean)} which uses {@link #DEFAULT_FILENAME_GENERATOR}.
	 */
	public boolean saveAll(@NotNull Plugin plugin, @NotNull String subdirectory, boolean overwrite) throws IOException {
		return saveAll(plugin, subdirectory, DEFAULT_FILENAME_GENERATOR, overwrite);
	}

	/**
	 * An overload of {@link #saveAll(Plugin, String, boolean)} which uses {@link #DEFAULT_SUBDIRECTORY_NAME} and
	 * {@link #DEFAULT_FILENAME_GENERATOR} by extension.
	 */
	public boolean saveAll(@NotNull Plugin plugin, boolean overwrite) throws IOException {
		return saveAll(plugin, DEFAULT_SUBDIRECTORY_NAME, overwrite);
	}

	/**
	 * An overload of {@link #saveAll(Plugin, boolean)} which defaults overwriting to true.
	 */
	public boolean saveAll(@NotNull Plugin plugin) throws IOException {
		return saveAll(plugin, true);
	}

}
