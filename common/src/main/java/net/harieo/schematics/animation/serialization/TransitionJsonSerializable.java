package net.harieo.schematics.animation.serialization;

import com.google.gson.JsonObject;
import net.harieo.schematics.animation.Transition;
import net.harieo.schematics.serialization.serializable.Serializable;

/**
 * A {@link Serializable} {@link Transition} into JSON format. The purpose of this interface is to allow implementing
 * classes to infer {@link Serializable} with the relevant parameters (which is otherwise impossible in Java).
 */
public interface TransitionJsonSerializable extends Serializable<Transition, JsonObject> {
}
