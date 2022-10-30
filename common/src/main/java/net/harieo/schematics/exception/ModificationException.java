package net.harieo.schematics.exception;

import net.harieo.schematics.modification.Modification;
import org.jetbrains.annotations.NotNull;

/**
 * An exception which occurs when the application of a {@link Modification} fails.
 *
 * @apiNote this is a {@link RuntimeException} because it should not occur if proper checks are performed before attempting
 * to apply the {@link Modification}. Therefore, it does not need to be a checked exception.
 */
public class ModificationException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "Failed to apply modification";

    private final Modification modification;

    public ModificationException(@NotNull Modification modification, @NotNull String message) {
        super(message);
        this.modification = modification;
    }

    public ModificationException(@NotNull Modification modification, @NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
        this.modification = modification;
    }

    /**
     * An overload of {@link ModificationException#ModificationException(Modification, String)} with the {@link #DEFAULT_MESSAGE}.
     *
     * @param modification the affected modification
     */
    public ModificationException(@NotNull Modification modification) {
        this(modification, DEFAULT_MESSAGE);
    }

    /**
     * An overload of {@link ModificationException#ModificationException(Modification, String, Throwable)} with the
     * {@link #DEFAULT_MESSAGE}.
     *
     * @param modification the affected modification
     * @param cause        the throwable cause of this exception
     */
    public ModificationException(@NotNull Modification modification, @NotNull Throwable cause) {
        this(modification, DEFAULT_MESSAGE, cause);
    }

    public Modification getModification() {
        return modification;
    }

}
