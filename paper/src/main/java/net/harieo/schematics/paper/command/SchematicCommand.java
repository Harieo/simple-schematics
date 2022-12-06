package net.harieo.schematics.paper.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.modification.RelativeModification;
import net.harieo.schematics.paper.SchematicsPlugin;
import net.harieo.schematics.paper.config.SchematicStorage;
import net.harieo.schematics.paper.modification.impl.BlockModification;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.position.Coordinate;
import net.harieo.schematics.position.Vector;
import net.harieo.schematics.schematic.Schematic;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CommandAlias("schematic|schematics|simpleschematics|simpleschematic|schema")
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

    @Subcommand("create|make|schematic")
    @CommandPermission("schematics.create")
    public void createSchematic(Player player,
                                @Name("schematic id") String schematicId) {
        SchematicStorage schematicStorage = plugin.getSchematicStorage();
        Optional<Schematic> optionalExistingSchematic = schematicStorage.getSchematic(schematicId);
        if (optionalExistingSchematic.isPresent()) {
            player.sendMessage(ChatColor.RED + "A schematic with that id already exists.");
            return;
        }

        persistence.getCuboid(player.getUniqueId()).ifPresentOrElse(cuboid -> {
            if (cuboid.isValid()) {
                BukkitCoordinate initialPosition = cuboid.getLowerCorner().orElseThrow();
                Set<RelativeModification<? extends Modification>> modifications = cuboid.getInnerCoordinates(1)
                        .stream()
                        .map(coordinate -> {
                            Block block = coordinate.toLocation().getBlock();
                            BlockModification blockModification = new BlockModification(cuboid.getWorld(), block.getType());
                            Vector vector = initialPosition.getRelativeVector(coordinate);
                            return new RelativeModification<>(blockModification, vector);
                        })
                        .collect(Collectors.toSet());

                Schematic schematic = new Schematic(schematicId, initialPosition, modifications);
                schematicStorage.addSchematic(schematic);
                player.sendMessage(ChatColor.GREEN + "The schematic has been stored with id " + schematicId + ".");
                player.sendMessage(ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/schematics save " + ChatColor.GRAY + "to save to file.");
            }
        }, () -> player.sendMessage(ChatColor.RED + "A schematic requires two positions to form a cuboid region. " +
                "You have none set."));
    }

}
