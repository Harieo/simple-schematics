package net.harieo.schematics.paper.tool;

import net.harieo.schematics.paper.command.SchematicCommandPersistence;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SchematicToolListener implements Listener {

    private final SchematicToolConfiguration schematicToolConfiguration;
    private final SchematicCommandPersistence schematicCommandPersistence;

    public SchematicToolListener(@NotNull SchematicToolConfiguration schematicToolConfiguration,
                                 @NotNull SchematicCommandPersistence schematicCommandPersistence) {
        this.schematicToolConfiguration = schematicToolConfiguration;
        this.schematicCommandPersistence = schematicCommandPersistence;
    }

    @EventHandler
    public void onToolUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && schematicToolConfiguration.getSchematicTool().map(item::isSimilar).orElse(false)) {
            if (player.hasPermission("schematics.tool.use")) {
                Block block = event.getClickedBlock();
                if (block == null) {
                    return;
                }

                Location clickLocation = block.getLocation();
                event.setCancelled(true);

                BukkitCoordinate clickCoordinate = new BukkitCoordinate(clickLocation);
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    schematicCommandPersistence.setUpperCorner(player.getUniqueId(), clickCoordinate);
                    player.sendMessage(ChatColor.GREEN + "Set lower corner (pos1) position to " + formatLocation(clickLocation));
                } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    schematicCommandPersistence.setLowerCorner(player.getUniqueId(), clickCoordinate);
                    player.sendMessage(ChatColor.GREEN + "Set upper corner (pos2) position to " + formatLocation(clickLocation));
                }
            }
        }
    }

    private String formatLocation(@NotNull Location location) {
        return "(" + location.getX() + "," + location.getY() + "," + location.getZ() + ")";
    }

}
