package net.harieo.schematics.paper.modification.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.harieo.schematics.paper.modification.BukkitModification;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.position.Coordinate;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link BukkitModification} which changes the {@link Block} type at the given {@link Coordinate}.
 */
public class BlockModification extends BukkitModification {

    public static final String MATERIAL_KEY = "material";

    private final Material blockMaterial;

    /**
     * Creates a {@link BukkitModification} in the given {@link World} to change a {@link Block} into the given {@link Material}.
     *
     * @param world         the world which this modification is taking place in
     * @param blockMaterial the material to set blocks to
     * @throws IllegalArgumentException if the provided material returns {@code Material#isBlock() == false}
     */
    public BlockModification(@NotNull World world, @NotNull Material blockMaterial) {
        super("block-edit", world);
        if (!blockMaterial.isBlock()) {
            throw new IllegalArgumentException("Not a block material: " + blockMaterial.name());
        }

        this.blockMaterial = blockMaterial;
    }

    @Override
    public boolean isAvailable(@NotNull BukkitCoordinate bukkitCoordinate) {
        return toBlock(bukkitCoordinate).getType() != blockMaterial;
    }

    @Override
    public void apply(@NotNull BukkitCoordinate bukkitCoordinate) {
        toBlock(bukkitCoordinate).setType(blockMaterial);
    }

    public Block toBlock(@NotNull BukkitCoordinate bukkitCoordinate) {
        return bukkitCoordinate.toLocation().getBlock();
    }

    @Override
    protected void addExtraSerializationData(@NotNull JsonObject serializedObject) {
        serializedObject.addProperty(MATERIAL_KEY, blockMaterial.name());
    }

}
