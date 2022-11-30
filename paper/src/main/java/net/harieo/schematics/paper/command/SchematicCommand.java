package net.harieo.schematics.paper.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.harieo.schematics.paper.SchematicsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

@CommandAlias("schematic|simpleschematics|simpleschematic|schema")
public class SchematicCommand extends BaseCommand {

    private final SchematicsPlugin plugin;

    private final SchematicCommandPersistence persistence = new SchematicCommandPersistence();

    public SchematicCommand(@NotNull SchematicsPlugin plugin) {
        this.plugin = plugin;
    }

    @Subcommand("tool|wand|schemtool|schemwand")
    @CommandPermission("schematics.tool.give")
    public void setTool(Player player) {
        ItemStack tool = plugin.getSchematicToolConfiguration().getSchematicTool().orElse(null);
        if (tool == null) {
            player.sendMessage(ChatColor.RED + "There is no schematic tool configured.");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        if (inventory.getItemInMainHand().getType().isEmpty()) { // If main hand is empty
            inventory.setItemInMainHand(tool); // Set the tool in main hand
        } else {
            inventory.addItem(tool); // Otherwise, add to next available slot
        }
    }

    @Subcommand("setpos|setposition")
    @CommandCompletion("@positions")
    @CommandPermission("schematics.position.set")
    public void setPosition(Player player, @Name("position") @Values("@positions") String position) {
        for (CommandPosition commandPosition : CommandPosition.values()) {
            if (commandPosition.getId().equalsIgnoreCase(position)) {
                commandPosition.apply(player, persistence);
                player.sendMessage(ChatColor.RED + "Set " + commandPosition.getId() + " to your location.");
                return;
            }
        }

        // If no positions are found...
        player.sendMessage(ChatColor.RED + "Unknown position: Expected: /... setpos <pos1/pos2>");
    }

}
