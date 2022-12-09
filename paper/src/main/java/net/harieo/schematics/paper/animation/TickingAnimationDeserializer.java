package net.harieo.schematics.paper.animation;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
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

    @Override
    public TickingAnimation deserializeToExactForm(@Nullable String id, @NotNull List<Transition> transitions) {
        return new TickingAnimation(plugin, id, transitions);
    }

}
