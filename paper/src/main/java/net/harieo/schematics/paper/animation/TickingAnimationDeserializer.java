package net.harieo.schematics.paper.animation;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.animation.impl.serializable.SerializableEmptyTransition;
import net.harieo.schematics.animation.impl.serializable.SerializableEmptyTransition.EmptyTransitionJsonSerializer;
import net.harieo.schematics.animation.impl.serializable.SerializableSchematicTransition.SchematicTransitionJsonSerializer;
import net.harieo.schematics.animation.serialization.EmptyTransitionDeserializer;
import net.harieo.schematics.animation.serialization.SchematicTransitionDeserializer;
import net.harieo.schematics.paper.transition.SerializableSoundTransition;
import net.harieo.schematics.paper.transition.SerializableSoundTransition.SoundTransitionJsonSerializer;
import net.harieo.schematics.paper.transition.SoundTransitionDeserializer;
import net.harieo.schematics.schematic.Schematic;
import net.harieo.schematics.serialization.Blueprint;
import net.harieo.schematics.serialization.impl.animation.AnimationJsonDeserializer;
import net.harieo.schematics.serialization.registry.BlueprintRegistry;

import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * An extension of {@link AnimationJsonDeserializer} which constructs {@link TickingAnimation}.
 */
public class TickingAnimationDeserializer extends AnimationJsonDeserializer<TickingAnimation> {

	private final Plugin plugin;

	/**
	 * An extension of {@link AnimationJsonDeserializer} which constructs {@link TickingAnimation}.
	 *
	 * @param plugin the plugin to pass to {@link TickingAnimation}
	 * @param blueprintRegistry the registry of blueprints for deserializing transitions
	 */
	public TickingAnimationDeserializer(@NotNull Plugin plugin,
			@NotNull BlueprintRegistry<Transition, JsonObject> blueprintRegistry) {
		super(blueprintRegistry);
		this.plugin = plugin;
	}

	public TickingAnimationDeserializer(@NotNull Plugin plugin,
			@NotNull Blueprint<Schematic, JsonObject> schematicJsonBlueprint) {
		this(plugin,
				new BlueprintRegistry<>(
						new Blueprint<>(new EmptyTransitionJsonSerializer(), new EmptyTransitionDeserializer()),
						new Blueprint<>(new SoundTransitionJsonSerializer(), new SoundTransitionDeserializer()),
						new Blueprint<>(new SchematicTransitionJsonSerializer(schematicJsonBlueprint.getSerializer()),
								new SchematicTransitionDeserializer(schematicJsonBlueprint.getDeserializer()))));
	}

	@Override
	public TickingAnimation deserializeToExactForm(@Nullable String id, @NotNull List<Transition> transitions) {
		return new TickingAnimation(plugin, id, transitions);
	}

}
