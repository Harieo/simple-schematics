package net.harieo.schematics.paper.command.animation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.animation.impl.basic.ScheduledAnimation;
import net.harieo.schematics.paper.animation.TickingAnimation;
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

    public AnimationCommand(@NotNull Plugin plugin) {
        this.plugin = plugin;
        this.persistence = new AnimationCommandPersistence(plugin);
    }

    @Subcommand("create|new")
    @CommandPermission("schematics.animation.create")
    public void createAnimation(Player player,
                                @Name("type") @Default(value = "ticking") String animationType,
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

}
