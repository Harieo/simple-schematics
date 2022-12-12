package net.harieo.schematics.paper.command.animation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.animation.impl.basic.ScheduledAnimation;
import net.harieo.schematics.paper.SchematicsPlugin;
import net.harieo.schematics.paper.animation.TickingAnimation;
import net.harieo.schematics.paper.command.transition.TransitionIntentRegistry;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Locale;

@Subcommand("animation|simpleanimation|animations|simpleanimations")
public class AnimationCommand extends BaseCommand {

    private final Plugin plugin;
    private final AnimationCommandPersistence persistence;
    private final TransitionIntentRegistry transitionIntentRegistry;

    public AnimationCommand(@NotNull SchematicsPlugin plugin) {
        this.plugin = plugin;
        this.persistence = new AnimationCommandPersistence(plugin);
        this.transitionIntentRegistry = plugin.getTransitionIntentRegistry();
    }

    @Subcommand("create|new")
    @CommandPermission("schematics.animation.create")
    public void createAnimation(Player player,
                                @Name("type") @Values("ticking|scheduled") @Default(value = "ticking") String animationType,
                                @Name("id") @Optional String animationId) {
        Animation animation;
        switch (animationType.toLowerCase(Locale.ROOT)) {
            case "ticking" -> animation = new TickingAnimation(plugin, animationId, Collections.emptyList());
            case "native", "scheduled" -> animation = new ScheduledAnimation(animationId, Collections.emptyList());
            default -> {
                player.sendMessage(ChatColor.RED + "Unknown animation type: " + animationType);
                return;
            }
        }

        persistence.setAnimation(player.getUniqueId(), animation);
        player.sendMessage(ChatColor.GREEN + "The " + animationType + " has been created.");
        player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/animation transition ... "
                + ChatColor.GRAY + "to add transitions.");
    }

    @Subcommand("transition|createtransition|addtransition")
    @CommandCompletion("@transitions")
    @CommandPermission("schematics.animation.transition")
    public void addTransition(Player player,
                              @Name("transition type") @Values("@transitions") String transitionId,
                              @Name("transition args") String[] args) {
        transitionIntentRegistry.getIntent(transitionId).ifPresentOrElse(transitionIntent -> {
            try {
                Transition transition = transitionIntent.createTransition(player, args);
                persistence.addTransition(player.getUniqueId(), transition);
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + e.getMessage());
            }
        }, () -> player.sendMessage(ChatColor.RED + "Unknown transition: " + transitionId));
    }

}
