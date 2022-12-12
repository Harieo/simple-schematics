package net.harieo.schematics.paper.animation;

import org.bukkit.plugin.Plugin;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.HashSet;
import java.util.Set;
import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.serialization.impl.animation.AnimationJsonDeserializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

public class AnimationStorage {

	private final AnimationJsonDeserializer<? extends Animation> animationJsonDeserializer;

	private final Set<Animation> animations = new HashSet<>();

	public AnimationStorage(@NotNull AnimationJsonDeserializer<? extends Animation> animationJsonDeserializer) {
		this.animationJsonDeserializer = animationJsonDeserializer;
	}

	public @Unmodifiable Set<Animation> getAnimations() {
		return ImmutableSet.copyOf(animations);
	}

	public void addAnimation(@NotNull Animation animation) {
		animations.add(animation);
	}

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

}
