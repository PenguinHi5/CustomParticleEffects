package io.github.penguinhi5.effect;

import org.bukkit.entity.Player;

import java.util.HashMap;

public interface ParticleEffect
{

    /**
     * Plays the particle effect for the specified player.
     *
     * @param player the player that is playing the particle effect
     * @param args additional arguments used to modify the particle effect
     */
    void play(Player player, HashMap<String, String> args);

    /**
     * Stops playing the particle effect if it is currently playing.
     */
    void stop();

}
