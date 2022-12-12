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
import net.harieo.schematics.serialization.impl.animation.AnimationJsonDeserializer;
import net.harieo.schematics.serialization.impl.animation.AnimationJsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public class AnimationStorage {

	public static String DEFAULT_SUBDIRECTORY_NAME = "animations";
	public static Function<Animation, String> DEFAULT_FILENAME_GENERATOR = animation -> animation.getId()
			.orElseThrow(() -> new IllegalStateException("Animation must have id to be saved")) + ".json";

	private final AnimationJsonSerializer animationJsonSerializer;
	private final AnimationJsonDeserializer<? extends Animation> animationJsonDeserializer;
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private final Set<Animation> animations = new HashSet<>();

	public AnimationStorage(@NotNull AnimationJsonSerializer animationJsonSerializer,
			@NotNull AnimationJsonDeserializer<? extends Animation> animationJsonDeserializer) {
		this.animationJsonSerializer = animationJsonSerializer;
		this.animationJsonDeserializer = animationJsonDeserializer;
	}

	public @Unmodifiable Set<Animation> getAnimations() {
		return ImmutableSet.copyOf(animations);
	}

	public void addAnimation(@NotNull Animation animation) {
		animations.add(animation);
	}

	// TODO properly format

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

	public void load(@NotNull Plugin plugin) throws IOException {
		load(plugin, DEFAULT_SUBDIRECTORY_NAME);
	}

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

	public boolean saveAll(@NotNull Plugin plugin, boolean overwrite) throws IOException {
		return saveAll(plugin, DEFAULT_SUBDIRECTORY_NAME, DEFAULT_FILENAME_GENERATOR, overwrite);
	}

}
