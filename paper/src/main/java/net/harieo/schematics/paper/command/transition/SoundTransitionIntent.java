package net.harieo.schematics.paper.command.transition;

import net.harieo.schematics.paper.transition.SoundTransition;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundTransitionIntent extends TransitionIntent<SoundTransition> {

    @Override
    public String getId() {
        return "sound";
    }

    @Override
    public SoundTransition createTransition(@NotNull Player player, String[] arguments) throws IllegalArgumentException {
        if (arguments.length < 1) {
            throw new IllegalArgumentException("Insufficient arguments. Expected: <sound> [volume] [pitch] " +
                    "[before:millis] [after:millis]");
        }

        String soundId = arguments[0];
        float volume = 1;
        float pitch = 1;
        try {
            if (arguments.length > 1) {
                volume = Float.parseFloat(arguments[1]);
            }

            if (arguments.length > 2) {
                pitch = Float.parseFloat(arguments[2]);
            }
        } catch (NumberFormatException ignored) {
            throw new IllegalArgumentException("Invalid volume or pitch number");
        }

        MillisecondData millisecondData = parseMillisecondData(arguments, 3);
        SoundTransition soundTransition = new SoundTransition(soundId, player.getLocation(),
                millisecondData.millisecondsBefore(), millisecondData.millisecondsAfter());
        soundTransition.setVolume(volume);
        soundTransition.setPitch(pitch);
        return soundTransition;
    }

}
