package net.harieo.schematics.paper.command;

import net.harieo.schematics.paper.position.BukkitCoordinate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

/**
 * Positions which can be set by {@link SchematicCommand}.
 */
public enum CommandPosition {

    LOWER("pos1", ((player, persistence) -> persistence.setLowerCorner(player.getUniqueId(),
            new BukkitCoordinate(player.getLocation())))),
    UPPER("pos2", ((player, persistence) -> persistence.setUpperCorner(player.getUniqueId(),
            new BukkitCoordinate(player.getLocation()))));

    final String id;
    private final BiConsumer<Player, SchematicCommandPersistence> biConsumer;

    /**
     * A position which edits a value in {@link SchematicCommandPersistence}.
     *
     * @param id the identifying string
     * @param persistenceModifyingBiConsumer the action to be taken when a player modifies the position
     */
    CommandPosition(@NotNull String id, BiConsumer<Player, SchematicCommandPersistence> persistenceModifyingBiConsumer) {
        this.id = id;
        this.biConsumer = persistenceModifyingBiConsumer;
    }

    /**
     * @return the identifying string
     */
    public String getId() {
        return id;
    }

    /**
     * Applies the player's request.
     *
     * @param player the player making the request
     * @param persistence the persistence manager
     */
    public void apply(@NotNull Player player, @NotNull SchematicCommandPersistence persistence) {
        biConsumer.accept(player, persistence);
    }

}
