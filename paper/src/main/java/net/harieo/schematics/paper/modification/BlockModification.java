package net.harieo.schematics.paper.modification;

import net.harieo.schematics.exception.ModificationException;
import net.harieo.schematics.modification.Modification;
import net.harieo.schematics.paper.position.BukkitCoordinate;
import net.harieo.schematics.position.Coordinate;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link Modification} which changes the {@link Block} type at the given {@link Coordinate}.
 */
public class BlockModification implements Modification {

    private final World world;
    private final Material blockMaterial;

    /**
     * Creates a {@link Modification} in the given {@link World} to change a {@link Block} into the given {@link Material}.
     *
     * @param world the world which this modification is taking place in
     * @param blockMaterial the material to set blocks to
     * @throws IllegalArgumentException if the provided material returns {@code Material#isBlock() == false}
     */
    public BlockModification(@NotNull World world, @NotNull Material blockMaterial) {
        if (!blockMaterial.isBlock()) {
            throw new IllegalArgumentException("Not a block material: " + blockMaterial.name());
        }

        this.world = world;
        this.blockMaterial = blockMaterial;
    }

    @Override
    public boolean isAvailable(@NotNull Coordinate coordinate) {
        return toBlock(coordinate).getType() != blockMaterial;
    }

    @Override
    public void apply(@NotNull Coordinate coordinate) throws ModificationException {
        toBlock(coordinate).setType(blockMaterial);
    }

    public Block toBlock(@NotNull Coordinate coordinate) {
        return new BukkitCoordinate(world, coordinate).toLocation().getBlock();
    }

}
