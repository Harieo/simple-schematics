package net.harieo.schematics.paper.tool;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * A configuration to load the {@link ItemStack} which players can use to set positions.
 */
public class SchematicToolConfiguration {

    private static final ItemStack DEFAULT_ITEM = new ItemStack(Material.WOODEN_AXE);

    static {
        ItemMeta meta = DEFAULT_ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Schematic Tool");
        meta.setLore(Lists.newArrayList(ChatColor.YELLOW + "Left Click " + ChatColor.GRAY + "to set Position 1.",
                ChatColor.GREEN + "Right Click " + ChatColor.GRAY + "to set Position 2."));
        DEFAULT_ITEM.setItemMeta(meta);
    }

    private ItemStack schematicToolItem;

    public Optional<ItemStack> getSchematicTool() {
        return Optional.ofNullable(schematicToolItem);
    }

    public void load(@NotNull Plugin plugin) throws IOException {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (configFile.exists()) {
            FileConfiguration configuration = YamlConfiguration.loadConfiguration(configFile);
            this.schematicToolItem = configuration.getItemStack("tool");
        } else {
            FileConfiguration configuration = new YamlConfiguration();
            configuration.set("tool", DEFAULT_ITEM);
            schematicToolItem = DEFAULT_ITEM;
            configuration.save(configFile);
        }
    }

}
