package net.harieo.schematics.paper.command.transition;

import net.harieo.schematics.animation.Transition;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A middle class which can convert an array of {@link String} arguments into a {@link Transition} based on the intention
 * of a {@link Player}.
 *
 * @param <T> the type of transition which the user intends to create
 */
public abstract class TransitionIntent<T extends Transition> {

    /**
     * A unique identifier which identifies this specific intention.
     *
     * @return the unique identifier for this intention
     */
    public abstract String getId();

    /**
     * Creates the transition from an array of {@link String} arguments.
     *
     * @param player the player intending to create a transition
     * @param arguments the string arguments
     * @return the transition
     * @throws IllegalArgumentException if the array of arguments is invalid
     */
    public abstract T createTransition(@NotNull Player player, @NotNull String[] arguments) throws IllegalArgumentException;

    /**
     * Parses the final millisecond data for {@link Transition} from an array of {@link String} arguments starting from
     * a specified index.
     *
     * @param arguments the array of string arguments
     * @param startingIndex the starting index in the array
     * @return the millisecond data parsed, with default values as 0 if not present
     */
    protected MillisecondData parseMillisecondData(@NotNull String[] arguments, int startingIndex) {
        long millisecondsBefore = 0;
        long millisecondsAfter = 0;
        try {
            if (arguments.length > startingIndex) {
                millisecondsBefore = Long.parseLong(arguments[startingIndex]);
            }

            if (arguments.length > startingIndex + 1) {
                millisecondsAfter = Long.parseLong(arguments[startingIndex + 1]);
            }
        } catch (NumberFormatException ignored) {
            throw new IllegalArgumentException("Invalid number of milliseconds");
        }

        return new MillisecondData(millisecondsBefore, millisecondsAfter);
    }

    public record MillisecondData(long millisecondsBefore, long millisecondsAfter) {

    }

}
