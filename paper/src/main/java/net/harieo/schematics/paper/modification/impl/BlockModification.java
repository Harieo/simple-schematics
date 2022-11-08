package net.harieo.schematics.paper.modification.impl;

import net.harieo.schematics.paper.modification.BukkitModification;
import net.harieo.schematics.paper.modification.impl.blueprint.BukkitModificationJsonBlueprint;
import net.harieo.schematics.paper.modification.impl.deserializer.BlockModificationJsonDeserializer;
import net.harieo.schematics.paper.modification.impl.serializer.BlockModificationJsonSerializer;
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
    public static final BukkitModificationJsonBlueprint<BlockModification> BLUEPRINT =
            new BukkitModificationJsonBlueprint<>(
                    new BlockModificationJsonSerializer(),
                    new BlockModificationJsonDeserializer()
            );

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

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    @Override
    public boolean isAvailable(@NotNull BukkitCoordinate bukkitCoordinate) {
        return toBlock(bukkitCoordinate).getType() != blockMaterial;
    }

    @Override
    public void apply(@NotNull BukkitCoordinate bukkitCoordinate) {
        toBlock(bukkitCoordinate).setType(blockMaterial);
    }

    @Override
    public BukkitModificationJsonBlueprint<? extends BukkitModification> getJsonBlueprint() {
        return BLUEPRINT;
    }

    public Block toBlock(@NotNull BukkitCoordinate bukkitCoordinate) {
        return bukkitCoordinate.toLocation().getBlock();
    }

}
