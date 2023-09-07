package io.github.penguinhi5.effect;

import io.github.penguinhi5.CustomParticleEffects;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class LineParticleEffect implements ParticleEffect
{

    private CustomParticleEffects _plugin;
    private int _taskID = -1;
    private Location _loc1, _loc2;

    public LineParticleEffect(CustomParticleEffects plugin)
    {
        _plugin = plugin;
    }

    @Override
    public void play(Player player, HashMap<String, String> args)
    {
        // reset effect
        if (args.containsKey("reset"))
        {
            _loc1 = null;
            _loc2 = null;
            stop();
            return;
        }

        // set loc1
        if (_loc1 == null)
        {
            _loc1 = player.getLocation();
            return;
        }

        // update loc2 and play effect
        _loc2 = player.getLocation();
        stop();
        runEffect(args);
    }

    @Override
    public void stop()
    {
        if (_taskID != -1)
        {
            Bukkit.getScheduler().cancelTask(_taskID);
            _taskID = -1;
        }
    }

    private void runEffect(HashMap<String, String> args)
    {
        // properties
        int speed = 1;
        double distBetweenParticle = 0.25;
        int particleCount = (int)Math.max(Math.round(_loc2.distance(_loc1) / distBetweenParticle), 1);

        // spawn particles
        _taskID = new BukkitRunnable()
        {
            // compute dist between particles
            double xDist = (_loc2.getX() - _loc1.getX()) / particleCount;
            double yDist = (_loc2.getY() - _loc1.getY()) / particleCount;
            double zDist = (_loc2.getZ() - _loc1.getZ()) / particleCount;
            int idx = 0;

            @Override
            public void run()
            {
                // update idx
                if (++idx >= particleCount)
                    idx = 0;

                // compute particle coordinates
                double x = _loc1.getX() + xDist * idx;
                double y = _loc1.getY() + yDist * idx;
                double z = _loc1.getZ() + zDist * idx;

                // spawn particles
                _loc1.getWorld().spawnParticle(Particle.COMPOSTER, x, y, z, 1, 0, 0, 0);
                _loc1.getWorld().spawnParticle(Particle.CRIMSON_SPORE, x, y, z, 1, 0.5, 0.5, 0.5);
                _loc1.getWorld().spawnParticle(Particle.FALLING_OBSIDIAN_TEAR, x, y, z, 1, 0.5, 0.5, 0.5);
            }
        }.runTaskTimer(_plugin, 0, speed).getTaskId();
    }
}
