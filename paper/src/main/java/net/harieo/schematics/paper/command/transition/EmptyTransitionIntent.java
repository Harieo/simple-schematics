package net.harieo.schematics.paper.command.transition;

import net.harieo.schematics.animation.impl.basic.EmptyTransition;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EmptyTransitionIntent extends TransitionIntent<EmptyTransition> {

    @Override
    public String getId() {
        return "delay";
    }

    @Override
    public EmptyTransition createTransition(@NotNull Player player, String[] arguments) throws IllegalArgumentException {
        MillisecondData millisecondData = parseMillisecondData(arguments, 0);
        return new EmptyTransition(millisecondData.millisecondsBefore(), millisecondData.millisecondsAfter());
    }

}
