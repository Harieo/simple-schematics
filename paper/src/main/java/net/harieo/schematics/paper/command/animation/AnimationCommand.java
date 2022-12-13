package net.harieo.schematics.paper.command.animation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import java.io.IOException;
import net.harieo.schematics.animation.Animation;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.animation.impl.basic.ScheduledAnimation;
import net.harieo.schematics.paper.SchematicsPlugin;
import net.harieo.schematics.paper.animation.AnimationStorage;
import net.harieo.schematics.paper.animation.TickingAnimation;
import net.harieo.schematics.paper.command.transition.TransitionIntentRegistry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Locale;

@CommandAlias("animation|simpleanimation|animations|simpleanimations")
public class AnimationCommand extends BaseCommand {

	private final Plugin plugin;
	private final AnimationStorage animationStorage;
	private final AnimationCommandPersistence persistence;
	private final TransitionIntentRegistry transitionIntentRegistry;

	public AnimationCommand(@NotNull SchematicsPlugin plugin) {
		this.plugin = plugin;
		this.animationStorage = plugin.getAnimationStorage();
		this.persistence = new AnimationCommandPersistence(plugin);
		this.transitionIntentRegistry = plugin.getTransitionIntentRegistry();
	}

	@Subcommand("create|new")
	@CommandPermission("schematics.animation.create")
	public void createAnimation(Player player,
			@Name("id") String animationId,
			@Name("type") @Values("ticking|scheduled") @Default(value = "ticking") String animationType) {
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
		player.sendMessage(ChatColor.GREEN + "The " + animationType + " animation has been created.");
		player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/animation transition ... "
				+ ChatColor.GRAY + "to add transitions.");
	}

	@Subcommand("setid|setname")
	@CommandPermission("schematics.animation.create")
	public void setAnimationId(Player player, @Name("id") String animationId) {
		persistence.setAnimationId(player.getUniqueId(), animationId);
		player.sendMessage(ChatColor.RED + "Set the id of your animation to " + animationId + ".");
	}

	@Subcommand("list|info|listtransitions|transitions")
	@CommandPermission("schematics.animation.create")
	@Default
	public void listTransitions(Player player) {
		persistence.getAnimation(player.getUniqueId()).ifPresentOrElse(animation -> {
			// Show animation id
			player.sendMessage(ChatColor.GREEN + "Animation Id: "
					+ animation.getId().orElse(ChatColor.YELLOW + "[Not Set]"));

			int index = 1;
			// List transitions
			for (Transition transition : animation.getAllTransitions()) {
				StringBuilder builder = new StringBuilder("    ");
				builder.append(ChatColor.GRAY);
				builder.append("(");
				builder.append(index++);
				builder.append(") ");

				if (transition.hasTimeBefore()) {
					builder.append(ChatColor.DARK_AQUA);
					builder.append(formatMillisecondsAsSeconds(transition.getMillisecondsBefore()));
					builder.append(" -> ");
				}

				builder.append(ChatColor.LIGHT_PURPLE);
				builder.append(transition.getId().orElse("Unknown Transition"));

				if (transition.hasTimeAfter()) {
					builder.append(ChatColor.BLUE);
					builder.append(" -> ");
					builder.append(formatMillisecondsAsSeconds(transition.getMillisecondsAfter()));
				}
				player.sendMessage(builder.toString());
			}

			// Add extra help message for how to add transitions
			player.sendMessage(ChatColor.GRAY + "    To add another transition, use "
					+ ChatColor.YELLOW + "/animation transition <type> [args...]");
		}, () -> player.sendMessage(ChatColor.RED + "You have not yet created an animation."));
	}

	/**
	 * Formats an amount of milliseconds to a user-readable {@link String} of seconds.
	 *
	 * @param milliseconds the amount of milliseconds
	 * @return the user-readable amount of seconds
	 */
	private String formatMillisecondsAsSeconds(long milliseconds) {
		long seconds = milliseconds / 1000;
		return "[" + seconds + " seconds" + "]";
	}

	@Subcommand("transition|createtransition|addtransition")
	@CommandCompletion("@transitions")
	@CommandPermission("schematics.animation.create")
	public void addTransition(Player player,
			@Name("transition type") @Values("@transitions") String transitionId,
			@Name("transition args") String[] args) {
		transitionIntentRegistry.getIntent(transitionId).ifPresentOrElse(transitionIntent -> {
			try {
				Transition transition = transitionIntent.createTransition(player, args);
				persistence.addTransition(player.getUniqueId(), transition);
				player.sendMessage(ChatColor.GREEN + "Added a " + transitionId + " transition.");
			} catch (IllegalArgumentException e) {
				player.sendMessage(ChatColor.RED + e.getMessage());
			}
		}, () -> player.sendMessage(ChatColor.RED + "Unknown transition: " + transitionId));
	}

	@Subcommand("save|saveanimation")
	@CommandPermission("schematics.animation.create")
	public void saveAnimation(Player player) {
		persistence.getAnimation(player.getUniqueId()).ifPresentOrElse(animation -> {
			if (animation.getId().isEmpty()) {
				player.sendMessage(ChatColor.RED + "An animation must have an id to be saved.");
				return;
			} else if (animation.getAllTransitions().isEmpty()) {
				player.sendMessage(ChatColor.RED + "An animation must have at least 1 transition.");
				return;
			}

			animationStorage.addAnimation(animation);
			try {
				animationStorage.saveAll(plugin, true);
				player.sendMessage(ChatColor.GREEN + "Saved animation with id " + animation.getId().orElseThrow());
			} catch (IOException e) {
				e.printStackTrace();
				player.sendMessage(
						ChatColor.RED + "An error occurred saving animations to file. Check console for details.");
			}
		}, () -> player.sendMessage(ChatColor.RED + "You are not creating an animation."));
	}

}
