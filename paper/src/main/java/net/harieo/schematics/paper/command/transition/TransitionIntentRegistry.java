package net.harieo.schematics.paper.command.transition;

import co.aikar.commands.CommandManager;
import co.aikar.commands.PaperCommandManager;
import com.google.common.collect.Sets;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.paper.SchematicsPlugin;
import net.harieo.schematics.paper.position.BukkitJsonCoordinateBlueprint;
import net.harieo.schematics.paper.position.BukkitJsonCoordinateBlueprint.BukkitJsonCoordinateSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Stores and provides functions to search through a set of {@link TransitionIntent}.
 */
public class TransitionIntentRegistry {

    private final Set<TransitionIntent<? extends Transition>> register;

    /**
     * Creates the registry with a default set of known intents.
     *
     * @param plugin the schematic plugin
     */
    public TransitionIntentRegistry(@NotNull SchematicsPlugin plugin) {
        this.register = Sets.newHashSet(new SoundTransitionIntent(),
                new EmptyTransitionIntent(),
                new SchematicTransitionIntent(plugin.getSchematicStorage(), new BukkitJsonCoordinateSerializer()));
    }

    /**
     * Creates a blank register.
     */
    public TransitionIntentRegistry() {
        this.register = new HashSet<>();
    }

    /**
     * @return the register of intents
     */
    public Set<TransitionIntent<? extends Transition>> getRegister() {
        return register;
    }

    /**
     * Adds an intent to the register.
     *
     * @param intent the intent to add
     */
    public void registerIntent(@NotNull TransitionIntent<? extends Transition> intent) {
        register.add(intent);
    }

    /**
     * Removes an intent from the register.
     *
     * @param intent the intent to remove
     */
    public void removeIntent(@NotNull TransitionIntent<? extends Transition> intent) {
        register.remove(intent);
    }

    /**
     * Retrieves an intent from the register with the matching id.
     *
     * @param intentId the id to match to registered intents
     * @return the matching intent, if a match is present
     */
    public Optional<TransitionIntent<? extends Transition>> getIntent(@NotNull String intentId) {
        return getRegister().stream()
                .filter(intent -> intent.getId().equalsIgnoreCase(intentId))
                .findAny();
    }

    /**
     * Registers command completions for all intent ids stored in the register.
     *
     * @param id the id for the command completion
     * @param commandManager the command manager
     */
    public void registerCommandCompletions(@NotNull String id, @NotNull PaperCommandManager commandManager) {
        commandManager.getCommandCompletions().registerCompletion(id,
                handler -> getRegister().stream().map(TransitionIntent::getId).collect(Collectors.toSet()));
    }

}
