package net.harieo.schematics.paper.command.animation;

import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.paper.animation.TickingAnimation;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class AnimationCommandPersistence {

    private final Plugin plugin;
    private final Map<UUID, Animation> animationMap = new HashMap<>();

    public AnimationCommandPersistence(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    public Optional<Animation> getAnimation(@NotNull UUID uuid) {
        return Optional.ofNullable(animationMap.get(uuid));
    }

    public void setAnimation(@NotNull UUID uuid, @NotNull Animation animation) {
        animationMap.put(uuid, animation);
    }

    public void setAnimationId(@NotNull UUID uuid, @NotNull String id) {
        getAnimation(uuid).ifPresentOrElse(
                animation -> animation.setId(id),
                () -> setAnimation(uuid, new TickingAnimation(plugin, id, Collections.emptyList())));
    }

    public void addTransition(@NotNull UUID uuid, @NotNull Transition transition) {
        getAnimation(uuid).ifPresentOrElse(animation ->
                        animation.addTransition(transition),
                () -> setAnimation(uuid, new TickingAnimation(plugin, null, Collections.singletonList(transition))));
    }

    public void removeTransition(@NotNull UUID uuid, int index) {
        getAnimation(uuid).ifPresent(animation -> animation.removeTransition(index));
    }

}
